import { CommonModule } from '@angular/common';
import { AfterViewChecked, ChangeDetectorRef, Component, ElementRef, NgZone, OnDestroy, OnInit, ViewChild } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { MatTooltipModule } from '@angular/material/tooltip';
import { MessageComponent } from '../message-component/message-component';
import { BotAvatar } from '../bot-avatar/bot-avatar';
import { Message } from '../../models/message';
import { filter, Subscription } from 'rxjs';
import { ActivatedRoute, NavigationEnd, Router } from '@angular/router';
import { ApiService } from '../../services/api-service';
import { AuthService } from '../../services/auth-service';
import { WebsocketService } from '../../services/websocket-service';
import { ConversationEventService } from '../../services/conversation-event-service';
import { NotificationService } from '../../services/notification-service';
// import { FilePreviewPipe } from '../../pipes/file-preview-pipe';

interface SuggestedQuestion {
  text: string;
  icon?: string;
}

@Component({
  selector: 'app-chat',
  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatIconModule,
    MatProgressSpinnerModule,
    MatTooltipModule,
    BotAvatar,
    MessageComponent,
    // FilePreviewPipe
],
  templateUrl: './chat.html',
  styleUrl: './chat.css',
})
export class Chat implements OnInit, OnDestroy, AfterViewChecked {

  @ViewChild('messagesContainer') messagesContainer?: ElementRef;
  @ViewChild('messageInput') messageInput?: ElementRef;
  // @ViewChild('fileInput') fileInput?: ElementRef<HTMLInputElement>;

  conversationId: number | null = null;
  messages: Message[] = [];
  messageContent = '';
  loading = false;
  isSending = false;
  botTyping = false;
  isCreating = false;
  shouldScrollToBottom = false;
  // selectedFile: File | null = null;
  // uploadProgress = 0;
  // fileInputKey = 0; // ✅ Force le re-render de l'input
  // isUploading = false

  suggestedQuestions: SuggestedQuestion[] = [
    { text: "Comment puis-je améliorer la qualité de mon sommeil et établir un rythme de sommeil plus régulier ?" },
    { text: "Quels sont les moyens efficaces d'augmenter mon niveau d'activité physique compte tenu de mon mode de vie actuel ?" },
    { text: "Comment gérer efficacement le stress et l'anxiété au quotidien ?" },
    { text: "Quelles sont les meilleures pratiques pour une alimentation équilibrée ?" }
  ];

  private routeSub?: Subscription;
  private wsSub?: Subscription;
  private routerSub?: Subscription;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private apiService: ApiService,
    private authService: AuthService,
    private wsService: WebsocketService,
    private convEventService: ConversationEventService,
    private cdr: ChangeDetectorRef,
    private notification: NotificationService,
    private zone: NgZone
  ) {}

  ngOnInit(): void {
    // ✅ Écoute les params (navigation vers /chat/:id)
    this.routeSub = this.route.params.subscribe(params => {
      const newId = params['id'] ? +params['id'] : null;
      this.handleNavigation(newId);
    });

    this.routerSub = this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      const url = this.router.url;
      if (url === '/chat' || url === '/chat/') {
        this.handleNavigation(null);
      }
    });

    // ✅ Écoute aussi /chat sans ID (params n'émet pas dans ce cas)
    this.router.events.pipe(
      filter(event => event instanceof NavigationEnd)
    ).subscribe(() => {
      const url = this.router.url;
      if (url === '/chat' || url === '/chat/') {
        this.handleNavigation(null);
      }
    });
  }

  private handleNavigation(newId: number | null): void {
    console.log('Navigation vers:', newId);

    this.conversationId = null;
    this.messages = [];
    this.botTyping = false;
    this.isSending = false;
    this.loading = false;
    this.isCreating = false;
    this.cdr.detectChanges();

    if (newId) {
      this.conversationId = newId;
      this.cdr.detectChanges();
      this.loadMessages();
      this.setupWebSocket();
    }
  }

  ngAfterViewChecked(): void {
    if (this.shouldScrollToBottom) {
      this.scrollToBottom();
      this.shouldScrollToBottom = false;
    }
  }

  ngOnDestroy(): void {
    this.routeSub?.unsubscribe();
    this.wsSub?.unsubscribe();
    this.routerSub?.unsubscribe(); 
  }

  showWelcome(): boolean {
    return !this.conversationId && !this.isCreating;
  }

  selectSuggestion(question: string): void {
    const user = this.authService.currentUserValue;
    if (!user) return;

    this.isCreating = true;
    this.messages = [];
    this.cdr.detectChanges(); // ✅ Cache suggestions immédiatement

    const title = question.length > 50
      ? question.substring(0, 50) + '...'
      : question;

    this.apiService.createConversation(user.id, title).subscribe({
      next: (conversation) => {
        this.convEventService.emitNewConversation(conversation);
        this.conversationId = conversation.id;
        this.cdr.detectChanges();

        this.notification.success("Conversation créée");

        this.router.navigate(['/chat', conversation.id]).then(() => {
          // ✅ Envoyer APRÈS que la navigation soit confirmée
          this._sendToApi(question);
        });
      },
      error: (err) => {
        console.error('Erreur:', err);
        this.notification.error("Impossible de créer la conversation");
        this.isCreating = false;
        this.cdr.detectChanges();
      }
    });
  }

  loadMessages(): void {
    if (!this.conversationId) return;
    this.loading = true;
    this.cdr.detectChanges();

    this.apiService.getConversationMessage(this.conversationId).subscribe({
      next: (messages) => {
        this.messages = [...messages]; // ✅ Nouvelle référence tableau
        this.shouldScrollToBottom = true;
        this.loading = false;
        this.cdr.detectChanges(); // ✅ Force le rendu
      },
      error: (err) => {
        console.error('Erreur chargement:', err);
        this.loading = false;
        this.cdr.detectChanges();
      }
    });
  }

  setupWebSocket(): void {
    if (!this.conversationId) return;
    this.wsSub?.unsubscribe();
    this.wsService.connect();
    this.wsService.subscribeToConversation(this.conversationId);

    this.wsSub = this.wsService.onMessage().subscribe({
      next: (message: Message) => {
        if (!this.messages.some(m => m.id === message.id)) {
          this.messages = [...this.messages, message]; // ✅ Nouvelle référence
          this.shouldScrollToBottom = true;
          this.cdr.detectChanges();
        }
      }
    });
  }

  // ✅ Sélection de fichier
  // onFileSelected(event: Event): void {
  //   const input = event.target as HTMLInputElement;
  //   if (input.files && input.files.length > 0) {
  //     const file = input.files[0];
      
  //     // Vérifier type
  //     if (!file.type.startsWith('image/')) {
  //       this.notification.error('Seules les images sont acceptées');
  //       return;
  //     }
      
  //     // Vérifier taille (5MB max)
  //     if (file.size > 5 * 1024 * 1024) {
  //       this.notification.error('L\'image est trop volumineuse (max 5MB)');
  //       return;
  //     }
      
  //     this.selectedFile = file;
  //   }
  // }

  // ✅ Retirer le fichier sélectionné
  // removeFile(): void {
  //   this.selectedFile = null;
  //   // if (this.fileInput) {
  //   //   this.fileInput.nativeElement.value = '';
  //   // }
  // }

  onSendMessage(): void {
    if (!this.messageContent.trim() || this.isSending) return;

    if (!this.conversationId) {
      const user = this.authService.currentUserValue;
      if (!user) return;

      const content = this.messageContent.trim();
      const title = content.length > 50 ? content.substring(0, 50) + '...' : content;

      this.messageContent = '';
      this.isCreating = true;
      this.cdr.detectChanges();

      this.apiService.createConversation(user.id, title).subscribe({
        next: (conversation) => {
          this.convEventService.emitNewConversation(conversation);
          this.conversationId = conversation.id;
          this.cdr.detectChanges();

          this.router.navigate(['/chat', conversation.id]).then(() => {
            this._sendToApi(content);
          });
        },
        error: (err) => {
          console.error('Erreur:', err);
          this.notification.error('Impossible de créer la conversation');
          this.isCreating = false;
          this.cdr.detectChanges();
        }
      });
      return;
    }

    const content = this.messageContent.trim();
    this.messageContent = '';
    
    setTimeout(() => {
      const ta = this.messageInput?.nativeElement;
      if (ta) ta.style.height = 'auto';
    }, 10);

    this._sendToApi(content);
  }

  onKeyDown(event: KeyboardEvent): void {
    if (event.key === 'Enter' && !event.shiftKey) {
      event.preventDefault();
      this.onSendMessage();
    }
  }

  canSend(): boolean {
    // return (this.messageContent.trim().length > 0 || this.selectedFile !== null) && 
    //       !this.isSending && 
    //       !this.isUploading;
    return this.messageContent.trim().length > 0 && !this.isSending;
  }

  autoResize(event: Event): void {
    const ta = event.target as HTMLTextAreaElement;
    ta.style.height = 'auto';
    ta.style.height = Math.min(ta.scrollHeight, 120) + 'px';
  }

  trackMessage(index: number, message: Message): number {
    return message.id ?? index;
  }

  private _sendToApi(content: string): void {
    this.messageContent = '';
    this.isSending = true;
    this.botTyping = true;
    this.isCreating = false; // ✅ Fin de la création
    this.cdr.detectChanges();

    setTimeout(() => {
      const ta = this.messageInput?.nativeElement;
      if (ta) ta.style.height = 'auto';
    }, 10);

    this.apiService.sendMessage(this.conversationId!, content).subscribe({
      next: (responseMessages) => {
        responseMessages.forEach(msg => {
          if (!this.messages.some(m => m.id === msg.id)) {
            this.messages = [...this.messages, msg]; // ✅ Nouvelle référence
          }
        });
        this.shouldScrollToBottom = true;
        this.isSending = false;
        this.botTyping = false;
        this.cdr.detectChanges();
      },
      error: (err) => {
        console.error('Erreur envoi:', err);
        this.notification.error("Erreur lors de l'envoi du message");
        this.isSending = false;
        this.botTyping = false;
        this.cdr.detectChanges();
      }
    });
  }

  // ✅ Nouvelle méthode pour envoyer avec fichier
  // private _sendWithFile(content: string, file: File): void {
  //   console.log('=== START SEND WITH FILE ===');
    
  //   this.isUploading = true;
  //   this.isSending = true;
  //   this.botTyping = true;
  //   this.isCreating = false;
  //   this.cdr.detectChanges();

  //   this.apiService.uploadFile(file).subscribe({
  //     next: (uploadResponse) => {
  //       console.log('Upload response:', uploadResponse);
        
  //       this.apiService.sendMessage(
  //         this.conversationId!,
  //         content,
  //         uploadResponse.fileUrl,
  //         uploadResponse.fileName
  //       ).subscribe({
  //         next: (responseMessages) => {
  //           this.zone.run(() => {
  //             console.log('=== FRONTEND RESPONSE ===');
  //             console.log('Messages count:', responseMessages.length);
              
  //             this.messages = [...this.messages, ...responseMessages];
  //             console.log('New messages array length:', this.messages.length);
              
  //             this.shouldScrollToBottom = true;
  //             this.isUploading = false;
  //             this.isSending = false;
  //             this.botTyping = false;
              
  //             // this.resetFileInput(); // ✅ Méthode dédiée
              
  //             this.cdr.markForCheck();
  //             this.cdr.detectChanges();
              
  //             console.log('=== END ===');
  //           });
  //         },
  //         error: (err) => {
  //           this.zone.run(() => {
  //             console.error('=== ERROR SENDING MESSAGE ===', err);
  //             this.notification.error('Erreur lors de l\'envoi du message');
  //             this.isUploading = false;
  //             this.isSending = false;
  //             this.botTyping = false;
              
  //             // this.resetFileInput(); // ✅ Méthode dédiée
              
  //             this.cdr.detectChanges();
  //           });
  //         }
  //       });
  //     },
  //     error: (err) => {
  //       this.zone.run(() => {
  //         console.error('=== ERROR UPLOADING FILE ===', err);
  //         this.notification.error('Erreur lors de l\'upload du fichier');
  //         this.isUploading = false;
  //         this.isSending = false;
  //         this.botTyping = false;
          
  //         // this.resetFileInput(); // ✅ Méthode dédiée
          
  //         this.cdr.detectChanges();
  //       });
  //     }
  //   });
  // }

  // ✅ Méthode dédiée pour reset l'input file
  // private resetFileInput(): void {
  //   this.selectedFile = null;
  //   this.fileInputKey++; // ✅ Force Angular à recréer l'input
  //   console.log('File input reset with key:', this.fileInputKey);
  // }

  private scrollToBottom(): void {
    setTimeout(() => {
      try {
        if (this.messagesContainer) {
          const el = this.messagesContainer.nativeElement;
          el.scrollTop = el.scrollHeight;
        }
      } catch (err) {}
    }, 100);
  }

}
