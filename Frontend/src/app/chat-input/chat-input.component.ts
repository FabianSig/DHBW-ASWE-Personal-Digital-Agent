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
  templateUrl: './chat-input.component.html',
  styleUrl: './chat-input.component.scss'
})
export class ChatInputComponent {

  searchTerm: string = '';
  error: string = '';

  constructor(private apiService: ApiService, private chatService: ChatService) {
  }

  onSearch(event: any): void {
    // Updates the searchTerm as the user types, enabling real-time input handling
    this.searchTerm = event.target.value;
  }

  handleChatGPTSearch(): void {
    this.chatService.addMessage(this.searchTerm, 'user');
    // Fetches a response from the ChatGPT API based on the user's input
    this.apiService.getChatGPTData(this.searchTerm).subscribe(response => {
      this.chatService.addMessage(response, 'chatgpt');

      // Initiates the Text-To-Speech process for the response to enhance user experience
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
