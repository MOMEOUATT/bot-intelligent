import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { User } from '../models/user';
import { Conversation } from '../models/conversation';
import { MessageDTO } from '../models/message';
import { Message } from '../components/message/message';

@Injectable({
  providedIn: 'root',
})
export class ApiService {

  private apiUrl = 'http://localhost:8080/api';

  constructor(private http: HttpClient){}

  createUser(email: string, username: string, password: string): Observable<User>{
    return this.http.post<User>(`${this.apiUrl}/users`, { email, username, password });
  }

  getUser(id: number): Observable<User>{
    return this.http.get<User>(`${this.apiUrl}/users/${id}`);
  }

  getUserByEmail(email: string): Observable<User>{
    return this.http.get<User>(`${this.apiUrl}/users/email/${email}`);
  }

  createConversation(userId: number, title: string): Observable<Conversation>{
    return this.http.post<Conversation>(`${this.apiUrl}/conversations`, {userId, title});
  }

  getUserConversation(userId: number): Observable<Conversation>{
    return this.http.get<Conversation>(`${this.apiUrl}/conversations/user/${userId}`);
  }

  getConversation(id: number): Observable<Conversation>{
    return this.http.get<Conversation>(`${this.apiUrl}/conversations/${id}`);
  }

  deleteConversation(id: number): Observable<void>{
    return this.http.delete<void>(`${this.apiUrl}/conversations/${id}`);
  }

  sendMessage(messageDto: MessageDTO): Observable<Message[]>{
    return this.http.post<Message[]>(`${this.apiUrl}/messages`, messageDto);
  }

  getConversationMessage(conversationId: number): Observable<Message[]>{
    return this.http.get<Message[]>(`${this.apiUrl}/messages/conversation/${conversationId}`);
  }

  searchMessages(conversationId: number, keyword: string): Observable<Message[]>{
    return this.http.get<Message[]>(`${this.apiUrl}/messages/conversations/${conversationId}/search`, {
      params: {keyword}
    });
  }
  
}
