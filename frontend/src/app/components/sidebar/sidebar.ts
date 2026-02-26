import { CommonModule } from '@angular/common';
import { Component, OnInit } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatMenuModule } from '@angular/material/menu';
import { MatFormFieldModule } from '@angular/material/form-field';
import { FormsModule } from '@angular/forms';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { Conversation } from '../../models/conversation';
import { ApiService } from '../../services/api-service';
import { AuthService } from '../../services/auth-service';
import { ActivatedRoute, Router, RouterModule } from '@angular/router';
import { MatCheckboxModule } from '@angular/material/checkbox';
import { MatTooltipModule } from '@angular/material/tooltip';
import { catchError, forkJoin, of, Subscription } from 'rxjs';
import { ConversationEventService } from '../../services/conversation-event-service';
import { NotificationService } from '../../services/notification-service';
import { MatDialog } from '@angular/material/dialog';
import { RenameDialog } from '../rename-dialog/rename-dialog';

@Component({
  selector: 'app-sidebar',
  imports: [
    CommonModule,
    FormsModule,
    MatButtonModule,
    MatIconModule,
    MatFormFieldModule,
    MatInputModule,
    MatMenuModule,
    MatProgressSpinnerModule,
    MatCheckboxModule,
    MatTooltipModule,
    RouterModule
  ],
  templateUrl: './sidebar.html',
  styleUrl: './sidebar.css',
})
export class Sidebar implements OnInit {

  conversations: Conversation[] = [];
  filteredConversations: Conversation[] = [];
  searchQuery: string = "";
  searchInMessages: boolean = true;
  searchingMessages: boolean = false;
  loading: boolean = false;
  currentConversationId: number | null = null;

  private eventSub?: Subscription;

  constructor(
    private apiService: ApiService,
    private authService: AuthService,
    private router: Router,
    private route: ActivatedRoute,
    private convEventService: ConversationEventService,
    private notification: NotificationService,
    private dialog: MatDialog
  ){}

  ngOnInit(): void {
    this.loadConversations();

    // ✅ Écoute simple du paramètre de route
    this.route.firstChild?.params.subscribe(params => {
      if (params['id']) this.currentConversationId = +params['id'];
    });

    // ✅ Nouvelles conversations créées par le chat
    this.eventSub = this.convEventService.newConversation$.subscribe(conversation => {
      const exists = this.conversations.some(c => c.id === conversation.id);
      if (!exists) {
        this.conversations.unshift(conversation);
        this.filteredConversations = [...this.conversations];
      }
      this.currentConversationId = conversation.id;
    });
  }

  loadConversations(): void {
    const user = this.authService.currentUserValue;
    if(!user){
      this.router.navigate(["/auth"]);
      return;
    }

    this.loading = true;

    this.apiService.getUserConversation(user.id).subscribe({
      next: (convs) => {
        this.conversations = convs.sort((a, b) =>
          new Date(b.updatedAt).getTime() - new Date(a.updatedAt).getTime()  
        );
        this.filteredConversations = [...this.conversations];
        this.loading = false;
      },
      error: (error) => {
        console.error("Erreur lors du chargement des conversations", error);
        this.loading = false;
      }
    });
  }

  onSearch(): void {
    if (!this.searchQuery.trim()) {
      this.filteredConversations = [...this.conversations];
      return;
    }

    const query = this.searchQuery.toLowerCase();

    if (this.searchInMessages) {
      // Recherche dans les titres ET les messages
      this.searchInConversationsAndMessages(query);
    } else {
      // Recherche uniquement dans les titres
      this.filteredConversations = this.conversations.filter(conv =>
        conv.title.toLowerCase().includes(query)
      );
    }
  }

  /**
 * Recherche dans les conversations et leurs messages
 */
  private searchInConversationsAndMessages(query: string): void {
    // Recherche dans les titres
    const titleMatches = this.conversations.filter(conv =>
      conv.title.toLowerCase().includes(query)
    );

    // Créer un tableau d'observables pour chercher dans les messages
    const searchObservables = this.conversations.map(conv =>
      this.apiService.searchMessages(conv.id, query).pipe(
        catchError(err => {
          console.error(`Erreur recherche conversation ${conv.id}:`, err);
          return of([]);
        })
      )
    );

    // Exécuter toutes les recherches en parallèle
    forkJoin(searchObservables).subscribe({
      next: (results) => {
        const messageMatches: Conversation[] = [];

        results.forEach((messages, index) => {
          if (messages && messages.length > 0) {
            const conv = this.conversations[index];
            if (!messageMatches.some(c => c.id === conv.id)) {
              messageMatches.push(conv);
            }
          }
        });

        // Combiner titres + messages
        const allMatches = [...titleMatches];
        messageMatches.forEach(conv => {
          if (!allMatches.some(c => c.id === conv.id)) {
            allMatches.push(conv);
          }
        });

        // Trier par date
        this.filteredConversations = allMatches.sort((a, b) =>
          new Date(b.updatedAt).getTime() - new Date(a.updatedAt).getTime()
        );

        console.log(`Recherche "${query}": ${allMatches.length} résultat(s)`);
      },
      error: (err) => {
        console.error('Erreur recherche globale:', err);
        // Au moins afficher les résultats de titre
        this.filteredConversations = titleMatches;
      }
    });

    // Afficher immédiatement les résultats de titre
    this.filteredConversations = titleMatches;
  }

  clearSearch(): void {
    this.searchQuery = "";
    this.onSearch();
  }

  onNewConversation(): void {
    // ✅ Ne crée PAS de conversation ici
    // Le chat s'en chargera quand l'utilisateur envoie son premier message
    this.currentConversationId = null;
    this.router.navigate(['/chat']);
  }

  onSelectConversation(conversation: Conversation): void {
    this.router.navigate(["/chat", conversation.id]);
  }

  isActive(conversationId: number): boolean {
    return this.router.url === `/chat/${conversationId}`;
  }

  onRenameConversation(conversation: Conversation): void {
    const dialogRef = this.dialog.open(RenameDialog, {
      width: '500px'
    });

    // Passer le titre après ouverture
    dialogRef.componentInstance.newTitle = conversation.title;

    dialogRef.afterClosed().subscribe(newTitle => {
      if (newTitle && newTitle !== conversation.title) {
        this.apiService.renameConversation(conversation.id, newTitle).subscribe({
          next: (updated) => {
            const index = this.conversations.findIndex(c => c.id === conversation.id);
            if (index !== -1) {
              this.conversations[index].title = updated.title;
              this.filteredConversations = [...this.conversations];
            }
            this.notification.success('Conversation renommée');
          },
          error: (err) => {
            console.error('Erreur:', err);
            this.notification.error('Impossible de renommer');
          }
        });
      }
    });
  }

  onDeleteConversation(id: number): void {
    if (!confirm('Supprimer cette conversation ?')) return;

    this.apiService.deleteConversation(id).subscribe({
      next: () => {
        this.conversations = this.conversations.filter(c => c.id !== id);
        this.filteredConversations = this.filteredConversations.filter(c => c.id !== id);

        this.notification.success("Conversation supprimée");
        
        if (this.currentConversationId === id) {
          this.router.navigate(['/chat']);
        }
      },
      error: (err) => {
        console.error('Erreur suppression:', err);
        this.notification.error("Impossible de supprimer");
      }
    });
  }

  formatDate(date: Date): string {
    const d = new Date(date);
    const now = new Date();
    const diff = now.getTime() - d.getTime();
    const days = Math.floor(diff/1000*60*60*24);

    if(days === 0){
      return "Aujourd'hui";
    } else if(days === 1) {
      return "Hier";
    } else if(days < 7){
      return `Il y'a ${days} jours`;
    } else {
      return d.toLocaleDateString("fr-FR", {day: "numeric", month: "short"});
    }
  }

}
