import {Injectable, signal} from '@angular/core';
import {ChatMessage} from '../interfaces/chat-message';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private messages: ChatMessage[] = [];
  private messageCounter = 0;
  isLoading = signal(false);

  addMessage(text: string, sender: 'user' | 'chatgpt') {
    this.isLoading.set(sender === 'user');  // When the user sends a message, we want to show that chatgpt is loading
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
