import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AudioRecorderService {
  private mediaRecorder!: MediaRecorder; // MediaRecorder instance for recording audio
  private audioChunks: Blob[] = []; // Array to store recorded audio chunks

  constructor() {}

  startRecording() {
    // Request access to the user's microphone
    navigator.mediaDevices.getUserMedia({ audio: true })
      .then(stream => {
        // Initialize the MediaRecorder with the audio stream
        this.mediaRecorder = new MediaRecorder(stream);
        this.audioChunks = []; // Reset audio chunks for a new recording

        // Collect audio data as it becomes available
        this.mediaRecorder.ondataavailable = (event) => {
          this.audioChunks.push(event.data);
        };

        this.mediaRecorder.start(); // Begin recording audio
      })
      .catch(error => {
        console.error('Error accessing microphone:', error);
      });
  }

  stopRecording(): Promise<Blob> {
    return new Promise((resolve, reject) => {
      // Ensure the MediaRecorder is initialized before stopping the recording
      if (!this.mediaRecorder) {
        reject(new TypeError('MediaRecorder is not initialized.'));
        return;
      }

      // Define what happens when the recording stops
      this.mediaRecorder.onstop = () => {
        // Combine the audio chunks into a single Blob for return
        const audioBlob = new Blob(this.audioChunks, { type: 'audio/ogg' });
        resolve(audioBlob); // Resolve the promise with the audio Blob
      };

      this.mediaRecorder.onerror = (event) => {
        reject(event); // Reject the promise if an error occurs during recording
      };

      this.mediaRecorder.stop(); // Stop the recording
    });
  }
}
