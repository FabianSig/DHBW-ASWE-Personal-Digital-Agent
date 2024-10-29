import { Component } from '@angular/core';
import {SearchBarComponent} from "../search-bar/search-bar.component";
import {AudioRecorderComponent} from '../audio-recorder/audio-recorder.component';
import {AudioResponse} from '../interfaces/audio-response';
import {ChatGPTResponse} from '../interfaces/chat-gptresponse';
import {MessageBoxComponent} from '../message-box/message-box.component';

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
  chatGPTResponse?: ChatGPTResponse;
  audioResponse?: AudioResponse;

  constructor(private messageBoxComponent: MessageBoxComponent) {
  }

  onChatGPTResponse(response: ChatGPTResponse): void {
    this.chatGPTResponse = response;
    this.messageBoxComponent.addGptMessage(this.chatGPTResponse.message.content);
  }

  onAudioResponse(response: AudioResponse): void {
    this.audioResponse = response;
    this.messageBoxComponent.addGptMessage(this.audioResponse.text);
  }
}
