import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/user';
import { Conversation } from '../models/conversation';
import { Message, MessageDTO } from '../models/message';

@Injectable({
  providedIn: 'root',
})
export class ApiService {

  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient){}

  // USER
  createUser(email: string, username: string, password: string): Observable<User>{
    return this.http.post<User>(`${this.apiUrl}/users`, { email, username, password });
  }

  getUser(id: number): Observable<User>{
    return this.http.get<User>(`${this.apiUrl}/users/${id}`);
  }

  getUserByEmail(email: string): Observable<User>{
    return this.http.get<User>(`${this.apiUrl}/users/email/${email}`);
  }

  updateUserProfile(id: number, email: string, username: string): Observable<User>{
    return this.http.put<User>(`${this.apiUrl}/users/${id}/profile`,{email, username});
  }

  changePassword(id:number, oldPassword: string, newPassword: string){
    return this.http.put<User>(`${this.apiUrl}/users/${id}/password`,{oldPassword, newPassword});
  }

  // CONVERSATIONS
  createConversation(userId: number, title: string): Observable<Conversation>{
    return this.http.post<Conversation>(`${this.apiUrl}/conversations`, {userId, title});
  }

  getUserConversation(userId: number): Observable<Conversation[]>{
    return this.http.get<Conversation[]>(`${this.apiUrl}/conversations/user/${userId}`);
  }

  getConversation(id: number): Observable<Conversation>{
    return this.http.get<Conversation>(`${this.apiUrl}/conversations/${id}`);
  }

  deleteConversation(id: number): Observable<void>{
    return this.http.delete<void>(`${this.apiUrl}/conversations/${id}`);
  }

  renameConversation(id: number, title: string): Observable<Conversation> {
    return this.http.put<Conversation>(`${this.apiUrl}/conversations/${id}/rename`, { title });
  }

  // MESSAGES
  sendMessage(conversationId: number, content: string): Observable<Message[]>{
    return this.http.post<Message[]>(`${this.apiUrl}/messages`, {
      conversationId,
      content
    });
  }

  getConversationMessage(conversationId: number): Observable<Message[]>{
    return this.http.get<Message[]>(`${this.apiUrl}/messages/conversation/${conversationId}`);
  }

  searchMessages(conversationId: number, keyword: string): Observable<Message[]>{
    return this.http.get<Message[]>(`${this.apiUrl}/messages/conversation/${conversationId}/search`, {
      params: {keyword}
    });
  }

  likeMessage(messageId: number): Observable<Message> {
    return this.http.put<Message>(`${this.apiUrl}/messages/${messageId}/like`, {});
  }

  dislikeMessage(messageId: number): Observable<Message> {
    return this.http.put<Message>(`${this.apiUrl}/messages/${messageId}/dislike`, {});
  }

  removeFeedback(messageId: number): Observable<Message> {
    return this.http.delete<Message>(`${this.apiUrl}/messages/${messageId}/feedback`);
  }
  
}
