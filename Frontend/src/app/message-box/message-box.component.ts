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

@Component({
  selector: 'app-message-box',
  standalone: true,
  imports: [],
  templateUrl: './message-box.component.html',
  styleUrl: './message-box.component.scss'
})
export class MessageBoxComponent implements OnInit, AfterViewChecked {
  messages: ChatMessage[] = [];
  prevMessagesLength = 0;
  isLoading: WritableSignal<boolean> | undefined ;
  @ViewChild('scrollAnchor') private scrollAnchor: ElementRef | undefined;

  constructor(private chatService: ChatService) {}

  ngOnInit() {
    this.isLoading = this.chatService.isLoading;
    this.messages = this.chatService.getMessages();
    this.prevMessagesLength = this.messages.length;
  }

  ngAfterViewChecked() {
    // Only scroll when messages are updated and not while typing
    if (this.messages.length > this.prevMessagesLength) {
      this.scrollToBottom();
    }
    this.prevMessagesLength = this.messages.length;
  }

  scrollToBottom() {
    // scroll to the bottom of the chat
    setTimeout(() => {
      this.scrollAnchor?.nativeElement.scrollIntoView({ behavior: 'smooth' });
    }, 500); // Delay of 500 milliseconds
  }
}
