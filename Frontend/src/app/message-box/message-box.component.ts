import { Component } from '@angular/core';
import {ChatMessage} from '../interfaces/chat-message';

@Component({
  selector: 'app-message-box',
  standalone: true,
  imports: [],
  templateUrl: './message-box.component.html',
  styleUrl: './message-box.component.scss'
})
export class MessageBoxComponent {
  messages: ChatMessage[] = [];
  private messageCounter: number = 0;

  constructor() {}

  addGptMessage(message: string) {
    this.messages.push({
      id: this.messageCounter++,
      text: message,
      sender: 'chatgpt'
    });
  }

  addUserMessage(message: string) {
    this.messages.push({
      id: this.messageCounter++,
      text: message,
      sender: 'user'
    });
  }
}
