import {Injectable} from '@angular/core';
import {ApiService} from './api.service';
import {ChatService} from './chat.service';
import {trigger} from '@angular/animations';

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
    let currentTimeInMs = new Date().getTime(); // Aktuelle Zeit in Millisekunden

    Object.entries(this.triggerMap).forEach(([routine, triggerTime]) => {
      const timeDifference = triggerTime - currentTimeInMs; // Berechnung der Differenz in Millisekunden
      console.log(`Processing ${ routine } with timedifference ${timeDifference}`)
      // Wenn die Zeit in der Zukunft liegt (negative Differenz), nichts tun
      if (timeDifference >= 0) {
        console.log(`Executing Trigger ${routine} in ${timeDifference/1000} seconds`)
          // Wenn die Zeit in der Zukunft liegt (positive Differenz), Timer setzen
          this.timoutReferenceMap[routine] =  setTimeout(() => {
          this.executeRoutine(routine);
        }, timeDifference); // Ausführen der Routine nach der berechneten Differenz
      }
    });
  }

  private executeRoutine(url: string) {
   this.apiService.executeCustomTriggerRoutine(url).subscribe((response: string) => {
     this.chatService.addMessage(response, 'chatgpt');
   });
  }

  private clearAllTriggers() {
    // Leere alle aktiven Timer
    Object.entries(this.timoutReferenceMap).forEach(([routine, timeoutRef]) => {
      clearTimeout(timeoutRef);
    });

    this.timoutReferenceMap = {}; // Leere das Referenzobjekt
  }

  public reload(): void {
    this.clearAllTriggers();
    this.setOffTrigger();
  }
}
