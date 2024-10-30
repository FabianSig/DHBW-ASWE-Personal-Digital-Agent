import {Component, inject} from '@angular/core';
import {SearchBarComponent} from "../search-bar/search-bar.component";
import {AudioRecorderComponent} from '../audio-recorder/audio-recorder.component';
import {AudioResponse} from '../interfaces/audio-response';
import {ChatGPTResponse} from '../interfaces/chat-gptresponse';
import {MessageBoxComponent} from '../message-box/message-box.component';
import {ApiService} from '../services/api.service';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [
    SearchBarComponent,
    AudioRecorderComponent,
    MessageBoxComponent
  ],
  templateUrl: './chat.component.html',
  styleUrl: './chat.component.scss'
})
export class ChatComponent {
  apiService: ApiService = inject(ApiService);
  chatGPTResponse?: string = this.apiService.chatGPTResponse();
  audioResponse?: AudioResponse;

  constructor(private messageBoxComponent: MessageBoxComponent) {
  }

  onChatGPTResponse(response: string): void {
    this.chatGPTResponse = response;
    this.messageBoxComponent.addGptMessage(this.chatGPTResponse);
  }

  onAudioResponse(response: AudioResponse): void {
    this.audioResponse = response;
    this.messageBoxComponent.addGptMessage(this.audioResponse.text);
  }
}
