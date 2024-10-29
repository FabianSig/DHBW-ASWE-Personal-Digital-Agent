import { Component, Output, EventEmitter } from '@angular/core';
import {ApiService} from '../services/api.service';
import {ChatGPTResponse} from '../interfaces/chat-gptresponse';
import {MessageBoxComponent} from '../message-box/message-box.component';

@Component({
  selector: 'app-search-bar',
  standalone: true,
  imports: [],
  templateUrl: './search-bar.component.html',
  styleUrl: './search-bar.component.scss'
})
export class SearchBarComponent {
  @Output() chatGPTResponseEmitter = new EventEmitter<ChatGPTResponse>();

  searchTerm: string = '';
  error: string = '';

  constructor(private apiService: ApiService, private messageBoxComponent: MessageBoxComponent) {}

  onSearch(event: any): void {
    this.searchTerm = event.target.value;
  }

  handleChatGPTSearch(): void {
    this.apiService.getChatGPTData(this.searchTerm).subscribe({
      next: (res) => {
        this.messageBoxComponent.addUserMessage(this.searchTerm);
        this.chatGPTResponseEmitter.emit(res as ChatGPTResponse);
      },
      error: (error) => {
        if (error.status === 401) {
          this.error = "Provide a valid API key.";
        }
        console.error('Error occurred:', error);
      }
    });
  }
}
