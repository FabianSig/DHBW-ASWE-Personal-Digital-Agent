import {Component, OnInit} from '@angular/core';
import {ChatMessage} from '../interfaces/chat-message';
import {ChatService} from '../services/chat.service';

@Component({
  selector: 'app-message-box',
  standalone: true,
  imports: [],
  templateUrl: './message-box.component.html',
  styleUrl: './message-box.component.scss'
})
export class MessageBoxComponent implements OnInit {
  messages: ChatMessage[] = [];

  constructor(private chatService: ChatService) {}

  ngOnInit() {
    this.messages = this.chatService.getMessages();
  }
}
