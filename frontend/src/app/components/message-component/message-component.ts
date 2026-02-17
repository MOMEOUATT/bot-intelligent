import { CommonModule } from '@angular/common';
import { Component, Input } from '@angular/core';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { MatTooltipModule } from '@angular/material/tooltip';
import { Message } from '../../models/message';

@Component({
  selector: 'app-message-component',
  imports: [
    CommonModule,
    MatIconModule,
    MatButtonModule,
    MatTooltipModule
  ],
  templateUrl: './message-component.html',
  styleUrl: './message-component.css',
})
export class MessageComponent {

  @Input() message!: Message;
  @Input() showActions: boolean = true;

  isLiked: boolean = false;
  isDisliked: boolean = false;
  
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

  onCopy(): void {
    if(navigator.clipboard){
      navigator.clipboard.writeText(this.message.content);
      // TODO: Afficher un toast "Copié !"
      console.log("Message copié");
    }
  }

  onLike(): void {
    this.isLiked = !this.isLiked;
    if(this.isLiked){
      this.isDisliked = false;
    }

    // TODO: Envoyer le feedback au backend
    console.log("Like: ", this.message.id);
  }

  onDislike(): void {
    this.isDisliked = !this.isDisliked;
    if(this.isDisliked){
      this.isLiked = false;
    }

    // TODO: Envoyer le feedback au backend
    console.log("Dislike: ", this.message.id);
  }

}
