import { Component, Output, EventEmitter } from '@angular/core';
import {ApiService} from '../services/api.service';
import {ChatGPTResponse} from '../interfaces/chat-gptresponse';

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
  apiKey: string = '';
  error: string = '';

  constructor(private apiService: ApiService) {}

  onSearch(event: any): void {
    this.searchTerm = event.target.value;
  }

  handleChatGPTSearch(): void {
    this.apiService.getChatGPTData(this.searchTerm, this.apiKey).subscribe({
      next: (res) => {
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

  handleKeySearch(): void {
    this.apiKey = this.searchTerm;
  }
}
