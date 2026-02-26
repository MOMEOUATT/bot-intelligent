import { Injectable } from '@angular/core';
import { Conversation } from '../models/conversation';
import { Subject } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class ConversationEventService {
  // Émis quand une nouvelle conversation est créée
  private newConversationSubject = new Subject<Conversation>();
  newConversation$ = this.newConversationSubject.asObservable();

  // Émis quand on veut reset le chat vers l'état vide
  private resetChatSubject = new Subject<void>();
  resetChat$ = this.resetChatSubject.asObservable();

  emitNewConversation(conversation: Conversation): void {
    this.newConversationSubject.next(conversation);
  }

  emitResetChat(): void {
    this.resetChatSubject.next();
  }
}
