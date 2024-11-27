import {Injectable} from '@angular/core';
import {ApiService} from './api.service';
import {ChatService} from './chat.service';

@Injectable({
  providedIn: 'root'
})
export class TriggerService {
  private triggerMap: { [url: string]: number } = {};
  private timoutReferenceMap: { [url: string]: ReturnType<typeof setTimeout> } = {};

  constructor(
    private apiService: ApiService,
    private chatService: ChatService
  ) {
    console.log("Reloading trigger")
    this.reload()
  }

  currentDate = new Date().toISOString().split('T')[0];


  setOffTrigger() {
    this.apiService.getTriggerData(this.currentDate).subscribe((data: any) => {
      console.log(`Getting Trigger for ${this.currentDate} with following data: ${JSON.stringify(data)}`)
        data.triggers.map((trigger: any) => {

          this.triggerMap[trigger.route] = new Date(trigger.time).getTime();
        });
      this.processTriggers();
    });
  }

  private processTriggers() {
    let currentTimeInMs = new Date().getTime(); // Current time in milliseconds

    Object.entries(this.triggerMap).forEach(([routine, triggerTime]) => {
      const timeDifference = triggerTime - currentTimeInMs; // Calculate the time difference in milliseconds
      console.log(`Processing ${ routine } with timedifference ${timeDifference}`)
      // If the time is in the past (negative difference), do nothing
      if (timeDifference >= 0) {
        console.log(`Executing Trigger ${routine} in ${timeDifference/1000} seconds`)
          this.apiService.executeCustomTriggerRoutine(routine).subscribe((response: string)=> {
          this.handleTriggerDisplayText(response, timeDifference)
          });// Execute the trigger routine
      }
    });
  }

  private handleTriggerDisplayText(text: string, timeDifference: any){
    this.apiService.getTtsAudioFile(text).subscribe({
      next: (res) => {
        // Create an object URL from the Blob
        const audioUrl = URL.createObjectURL(res);

        // Create an Audio object and set its source to the Blob URL
        const audio = new Audio(audioUrl);

        setTimeout(() => {
          this.chatService.addMessage(text, 'chatgpt');
          audio.play().catch((error) => {
            console.error('Error playing audio:', error);
          });

          // Revoke the URL after some time to free memory
          audio.onended = () => {
            URL.revokeObjectURL(audioUrl);
          };
        }, timeDifference);
        // Play the audio

      },
      error: (error) => {
        console.error('Error occurred:', error);
      }
    });
  }

  private clearAllTriggers() {
    // Clear all active timers
    Object.entries(this.timoutReferenceMap).forEach(([routine, timeoutRef]) => {
      clearTimeout(timeoutRef);
    });

    this.timoutReferenceMap = {}; // Empty the reference object
  }

  public reload(): void {
    this.clearAllTriggers();
    this.setOffTrigger();
  }
}
