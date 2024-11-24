import {Component} from '@angular/core';
import {ApiService} from '../services/api.service';
import {AudioRecorderComponent} from '../audio-recorder/audio-recorder.component';
import {AudioResponse} from '../interfaces/audio-response';
import {ChatService} from '../services/chat.service';
import {TtsService} from '../services/tts.service';
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

  constructor(private apiService: ApiService, private chatService: ChatService, private ttsService: TtsService) {
  }

  onSearch(event: any): void {
    this.searchTerm = event.target.value;
  }

  handleChatGPTSearch(): void {
    this.chatService.addMessage(this.searchTerm, 'user');
    this.apiService.getChatGPTData(this.searchTerm).subscribe(response => {
      this.chatService.addMessage(response, 'chatgpt');

      // Text-To-Speech
      this.apiService.getTtsAudioFile(response).subscribe({
        next: (res) => {
          // Create an object URL from the Blob
          const audioUrl = URL.createObjectURL(res);

          // Create an Audio object and set its source to the Blob URL
          const audio = new Audio(audioUrl);

          // Play the audio
          audio.play().catch((error) => {
            console.error('Error playing audio:', error);
          });

          // Revoke the URL after some time to free memory
          audio.onended = () => {
            URL.revokeObjectURL(audioUrl);
          };
        },
        error: (error) => {
          console.error('Error occurred:', error);
        }
      });
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
