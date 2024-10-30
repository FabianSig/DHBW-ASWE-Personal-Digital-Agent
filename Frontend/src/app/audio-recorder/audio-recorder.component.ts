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
      .then(() => {
        this.sendAudioToChatGPT();
      })
      .catch(error => {
        console.error('Error stopping recording:', error);
      });
  }

  private sendAudioToChatGPT(): void {
    if (!this.audioFile) {
      console.error('No audio file available');
      return;
    }

    this.apiService.getAudioData(this.audioFile).subscribe({
      next: (res) => {
        this.audioResponseEmitter.emit(res as AudioResponse);
      },
      error: (error) => {
        console.error('Error occurred:', error);
      }
    });
  }
}
