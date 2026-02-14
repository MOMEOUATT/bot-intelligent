import { Injectable } from '@angular/core';
import {Client, IMessage} from '@stomp/stompjs'
import { BehaviorSubject, Observable } from 'rxjs';
import { Message } from '../components/message/message';
import SockJS from 'sockjs-client';

@Injectable({
  providedIn: 'root',
})
export class WebsocketService {

  private stompClient: Client | null = null;
  private messageSubject = new BehaviorSubject<Message | null>(null);
  private message$: Observable<Message | null> = this.messageSubject.asObservable();

  private connected = false;

  constructor() {}

  connect(): void{
    if(this.connected){
      console.log("Déjà connecté");
      return;
    }

    const socket = new SockJS('http://localhost:8080/ws');

    this.stompClient = new Client({
      webSocketFactory: () => socket as any,
      debug: (str) => {
        console.log('STOMP: ' + str);
      },
      reconnectDelay: 5000,
      heartbeatIncoming: 4000,
      heartbeatOutgoing: 4000
    });

    this.stompClient.onConnect = (frame) => {
      console.log("COnnecté au WebSocket: " + frame);
      this.connected = true;
    }

    this.stompClient.onStompError = (frame) => {
      console.error("Erreur STOMP: " + frame.headers["message"]);
      console.error("Détails: " + frame.body);
    }

    this.stompClient.activate();
  }


  subscribeToConversation(conversationId: number): void {
    if(!this.stompClient || !this.connected){
      console.error("WebSocket pas connecté !");
      return;
    }

    const topic = `/topic/messages/${conversationId}`;

    this.stompClient.subscribe(topic, (message: IMessage) => {
      const receivedMessage: Message = JSON.parse(message.body);
      console.log("Nouveau message reçu: ", receivedMessage);
      this.messageSubject.next(receivedMessage);
    });

    console.log(`Abonné à ${topic}`);
  }

  sendMessage(conversationId: number, content: string): void {
    if(!this.stompClient || !this.connected){
      console.error("WebSocket pas connecté !");
      return;
    }

    const messageDto = {
      conversationId: conversationId,
      content: content
    };

    this.stompClient.publish({
      destination: '/app/chat',
      body: JSON.stringify(messageDto)
    });

    console.log("Message envoyé vers WebSocket");
  }


  disconnect(): void {
    if(this.stompClient){
      this.stompClient.deactivate();
      this.connected = false;
      console.log("Déconnecté du WebSocket")
    }
  }

  isConnected(): boolean {
    return this.connected;
  }

}
