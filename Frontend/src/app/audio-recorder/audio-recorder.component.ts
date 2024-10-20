import {Component, EventEmitter, Output} from '@angular/core';
import {ApiService} from '../services/api.service';
import {AudioResponse} from '../interfaces/audio-response';
import {AudioRecorderService} from '../services/audio-recorder.service';

@Component({
  selector: 'app-audio-recorder',
  standalone: true,
  imports: [],
  templateUrl: './audio-recorder.component.html',
  styleUrl: './audio-recorder.component.scss'
})
export class AudioRecorderComponent {
  @Output() audioResponseEmitter = new EventEmitter<AudioResponse>();

  searchTerm: string = '';
  apiKey: string = '';
  isRecording = false;
  audioFile?: Blob;

  constructor(private audioRecorderService: AudioRecorderService, private apiService: ApiService) {}

  startRecording() {
    this.isRecording = true;
    this.audioRecorderService.startRecording();
  }

  stopRecording() {
    this.isRecording = false;
    this.audioRecorderService.stopRecording()
      .then(audioBlob => {
        this.audioFile = audioBlob;
      })
      .catch(error => {
        console.error('Error stopping recording:', error);
      });
  }

  sendAudioToChatGPT(): void {
    if (!this.audioFile) {
      console.error('No audio file available');
      return;
    }

    this.apiService.getAudioData(this.audioFile, this.apiKey).subscribe({
      next: (res) => {
        this.audioResponseEmitter.emit(res as AudioResponse);
      },
      error: (error) => {
        console.error('Error occurred:', error);
      }
    });
  }

  onSearch(event: any): void {
    this.searchTerm = event.target.value;
  }

  handleKeySearch(): void {
    this.apiKey = this.searchTerm;
  }
}
