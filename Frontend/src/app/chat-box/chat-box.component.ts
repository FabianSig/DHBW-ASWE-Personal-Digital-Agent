import {
  AfterViewChecked,
  Component,
  ElementRef,
  OnInit,
  ViewChild,
  WritableSignal
} from '@angular/core';
import {ChatMessage} from '../interfaces/chat-message';
import {ChatService} from '../services/chat.service';
import {MarkdownModule} from 'ngx-markdown';

@Component({
  selector: 'app-message-box',
  standalone: true,
  imports: [MarkdownModule],
  templateUrl: './chat-box.component.html',
  styleUrl: './chat-box.component.scss'
})
export class ChatBoxComponent implements OnInit, AfterViewChecked {
  messages: ChatMessage[] = []; // Holds the chat messages
  prevMessagesLength = 0;
  isLoading: WritableSignal<boolean> | undefined; // holds the loading state, so that the loading bullets can be displayed
  @ViewChild('scrollAnchor') private scrollAnchor: ElementRef | undefined;

  constructor(private chatService: ChatService) {}

  ngOnInit() {
    // Initialize loading state and fetch existing messages from the chat service
    this.isLoading = this.chatService.isLoading;
    this.messages = this.chatService.getMessages();
    this.prevMessagesLength = this.messages.length;
  }

  ngAfterViewChecked() {
    // Scroll to the bottom only when new messages are added, avoiding interruptions while typing
    if (this.messages.length > this.prevMessagesLength) {
      this.scrollToBottom();
    }
    this.prevMessagesLength = this.messages.length; // Update previous message count for next check
  }

  scrollToBottom() {
    // Smoothly scroll to the bottom of the chat to ensure the latest message is visible
    setTimeout(() => {
      this.scrollAnchor?.nativeElement.scrollIntoView({ behavior: 'smooth' });
    }, 500); // Delay to allow message rendering before scrolling
  }
}
