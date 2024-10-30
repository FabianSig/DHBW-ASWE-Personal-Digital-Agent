import { Component } from '@angular/core';
import {SearchBarComponent} from "../search-bar/search-bar.component";
import {AudioRecorderComponent} from '../audio-recorder/audio-recorder.component';
import {AudioResponse} from '../interfaces/audio-response';
import {ChatGPTResponse} from '../interfaces/chat-gptresponse';

@Component({
  selector: 'app-chat',
  standalone: true,
  imports: [
    SearchBarComponent,
    AudioRecorderComponent
  ],
  templateUrl: './chat.component.html',
  styleUrl: './chat.component.scss'
})
export class ChatComponent {
  chatGPTResponse?: ChatGPTResponse;
  audioResponse?: AudioResponse;

  onChatGPTResponse(response: ChatGPTResponse): void {
    this.chatGPTResponse = response;
  }

  onAudioResponse(response: AudioResponse): void {
    this.audioResponse = response;
  }
}
