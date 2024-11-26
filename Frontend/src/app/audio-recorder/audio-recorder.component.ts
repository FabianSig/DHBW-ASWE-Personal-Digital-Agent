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

  // Track the recording state to manage UI and functionality
  isRecording = false;

  audioFile?: Blob;

  constructor(private audioRecorderService: AudioRecorderService, private apiService: ApiService) {}

  // Initiates the recording process and updates the state to reflect that recording is in progress
  startRecording() {
    this.isRecording = true;
    this.audioRecorderService.startRecording();
  }

  // Stops the recording and handles the audio file for further processing
  stopRecording() {
    this.isRecording = false; // Update state to indicate that recording has stopped
    this.audioRecorderService.stopRecording()
      .then(audioBlob => {
        this.audioFile = audioBlob; // Store the recorded audio for future use
      })
      .then(() => {
        this.sendAudioToChatGPT(); // Send the audio file for processing once recording is complete
      })
      .catch(error => {
        // Log any errors that occur during the stopping of the recording
        console.error('Error stopping recording:', error);
      });
  }

  // Sends the recorded audio to an external service and emits the response
  private sendAudioToChatGPT(): void {
    // Ensure there is an audio file to process; otherwise, log an error
    if (!this.audioFile) {
      console.error('No audio file available');
      return;
    }

    // Call the API service to send the audio data and handle the response
    this.apiService.getAudioData(this.audioFile).subscribe({
      next: (res) => {
        // Emit the processed audio response back to the parent component
        this.audioResponseEmitter.emit(res as AudioResponse);
      },
      error: (error) => {
        // Log any errors that occur during the API call
        console.error('Error occurred:', error);
      }
    });
  }
}
