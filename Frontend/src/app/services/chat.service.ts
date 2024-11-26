import {Injectable, signal} from '@angular/core';
import {ChatMessage} from '../interfaces/chat-message';

@Injectable({
  providedIn: 'root'
})
export class ChatService {
  private messages: ChatMessage[] = [];
  private messageCounter = 0;
  isLoading = signal(false);

  //Adds a new message to the chat and updates the loading state so the user knows when chatgpt is responding for the loading dots
  addMessage(text: string, sender: 'user' | 'chatgpt') {
    this.isLoading.set(sender === 'user');  // When the user sends a message, we want to show that chatgpt is loading
    this.messages.push({
      id: this.messageCounter++, // Increment the counter for a unique message ID
      text,
      sender
    });
  }

  // Returns the array of chat messages for display
  getMessages(): ChatMessage[] {
    return this.messages;
  }
}
