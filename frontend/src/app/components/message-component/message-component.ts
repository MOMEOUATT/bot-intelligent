import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Message } from '../../models/message';
import { ApiService } from '../../services/api-service';
import { AuthService } from '../../services/auth-service';
import { BotAvatar } from '../bot-avatar/bot-avatar';
import { NotificationService } from '../../services/notification-service';

@Component({
  selector: 'app-message-component',
  imports: [
    CommonModule,
    MatIconModule,
    MatButtonModule,
    MatTooltipModule,
    BotAvatar
  ],
  templateUrl: './message-component.html',
  styleUrl: './message-component.css',
})
export class MessageComponent {

  @Input() message!: Message;
  @Input() showActions: boolean = true;
  isProcessing = false;

  isLiked: boolean = false;
  isDisliked: boolean = false;

  constructor(
    private apiService: ApiService,
    private authService: AuthService,
    private notification : NotificationService
  ){}
  
  formatTime(date: Date | undefined): string {
    if(!date) return "";

    const d = new Date(date);
    const hours = d.getHours().toString().padStart(2, "0");
    const minutes = d.getMinutes().toString().padStart(2, "0");

    return `${hours}:${minutes}`;
  }

  formatMessageContent(): string {
    let content = this.message.content;

    // Échapper le HTML pour éviter XSS
    content = content
      .replace(/&/g, '&amp;')
      .replace(/</g, '&lt;')
      .replace(/>/g, '&gt;');

    // Gras : **texte** ou __texte__
    content = content.replace(/\*\*(.+?)\*\*/g, '<strong>$1</strong>');
    content = content.replace(/__(.+?)__/g, '<strong>$1</strong>');

    // Italique : *texte* ou _texte_
    content = content.replace(/\*(.+?)\*/g, '<em>$1</em>');
    content = content.replace(/_(.+?)_/g, '<em>$1</em>');

    // Code inline : `code`
    content = content.replace(/`(.+?)`/g, '<code>$1</code>');

    // Liens : [texte](url)
    content = content.replace(/\[(.+?)\]\((.+?)\)/g, '<a href="$2" target="_blank">$1</a>');

    // Sauts de ligne
    content = content.replace(/\n/g, '<br>');

    return content;
    
  }

  copied = false;

  onCopy(): void {
    navigator.clipboard.writeText(this.message.content).then(() => {
      this.copied = true;
      this.notification.success("Copié !")
      setTimeout(() => {
        this.copied = false;
      }, 2000);
    }).catch(err => {
      console.error('Erreur copie:', err);
      this.notification.error("Impossible de copier !")
    });
  }

  onLike(): void {
    if (this.isProcessing) return;

    this.isProcessing = true;

    // Si déjà liké, on retire le feedback
    if (this.message.liked) {
      this.apiService.removeFeedback(this.message.id).subscribe({
        next: (updatedMessage) => {
          this.message.liked = updatedMessage.liked;
          this.message.disliked = updatedMessage.disliked;
          this.notification.info("Feedback retiré");
          this.isProcessing = false;
        },
        error: (err) => {
          console.error('Erreur remove feedback:', err);
          this.notification.error("Erreur lors du feedback");
          this.isProcessing = false;
        }
      });
    } else {
      // Sinon on like
      this.apiService.likeMessage(this.message.id).subscribe({
        next: (updatedMessage) => {
          this.message.liked = updatedMessage.liked;
          this.message.disliked = updatedMessage.disliked;
          this.notification.success("Réponse utile !");
          this.isProcessing = false;
        },
        error: (err) => {
          console.error('Erreur like:', err);
          this.notification.error("Erreur lors du feedback");
          this.isProcessing = false;
        }
      });
    }
  }

  onDislike(): void {
    if (this.isProcessing) return;

    this.isProcessing = true;

    // Si déjà disliké, on retire le feedback
    if (this.message.disliked) {
      this.apiService.removeFeedback(this.message.id).subscribe({
        next: (updatedMessage) => {
          this.message.liked = updatedMessage.liked;
          this.message.disliked = updatedMessage.disliked;
          this.notification.info("Feedback retiré");
          this.isProcessing = false;
        },
        error: (err) => {
          console.error('Erreur remove feedback:', err);
          this.notification.error("Erreur lors du feedback");
          this.isProcessing = false;
        }
      });
    } else {
      // Sinon on dislike
      this.apiService.dislikeMessage(this.message.id).subscribe({
        next: (updatedMessage) => {
          this.message.liked = updatedMessage.liked;
          this.message.disliked = updatedMessage.disliked;
          this.notification.success("Réponse inutile !");
          this.isProcessing = false;
        },
        error: (err) => {
          console.error('Erreur dislike:', err);
          this.notification.error("Erreur lors du feedback");
          this.isProcessing = false;
        }
      });
    }
  }

  getUserInitial(): string {
    const user = this.authService.currentUserValue;
    return user?.username?.charAt(0).toUpperCase() || 'U';
  }

}
