import {Component} from '@angular/core';
import {ApiService} from '../services/api.service';
import {AudioRecorderComponent} from '../audio-recorder/audio-recorder.component';
import {AudioResponse} from '../interfaces/audio-response';
import {ChatService} from '../services/chat.service';
import {FormsModule} from '@angular/forms';

@Component({
  selector: 'app-search-bar',
  standalone: true,
  imports: [
    AudioRecorderComponent,
    FormsModule
  ],
  templateUrl: './search-bar.component.html',
  styleUrl: './search-bar.component.scss'
})
export class SearchBarComponent {

  searchTerm: string = '';
  error: string = '';

  constructor(private apiService: ApiService, private chatService: ChatService) {
  }

  onSearch(event: any): void {
    this.searchTerm = event.target.value;
  }

  handleChatGPTSearch(): void {
    this.chatService.addMessage(this.searchTerm, 'user');
    this.apiService.getChatGPTData(this.searchTerm).subscribe(response => {
      this.chatService.addMessage(response, 'chatgpt');
    })
    this.searchTerm = '';
  }

  onAudioResponse(response: AudioResponse): void {
    this.chatService.addMessage(response.text, 'user');
    this.apiService.getChatGPTData(response.text).subscribe(response => {
      this.chatService.addMessage(response, 'chatgpt');
    })

  }
}
