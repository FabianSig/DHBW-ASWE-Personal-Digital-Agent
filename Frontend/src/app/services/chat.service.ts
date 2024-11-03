import { Injectable } from '@angular/core';
import {ChatMessage} from '../interfaces/chat-message';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private messages: ChatMessage[] = [];
  private messageCounter = 0;

  addMessage(text: string, sender: 'user' | 'chatgpt') {
    this.messages.push({
      id: this.messageCounter++,
      text,
      sender
    });
  }

  getMessages(): ChatMessage[] {
    return this.messages;
  }
}
