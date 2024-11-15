import {Injectable} from '@angular/core';
import {ApiService} from './api.service';
import {ChatService} from './chat.service';
import {interval} from 'rxjs';

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
    this.clearAllTriggers();
    this.setOffTrigger();
  }

  currentDate = new Date().toISOString().split('T')[0];

  setOffTrigger() {
  //  this.prefferedMorningTime(); // Schauen und Setzen ob eine Morgenzeit in den Einstellungen gesetzt ist
    this.apiService.getTriggerData(this.currentDate).subscribe((data: any) => {
        data.triggers.map((trigger: any) => {
          this.triggerMap[trigger.route] = new Date(trigger.time).getTime();
          console.log(`Routine ${trigger.route} wird um ${this.triggerMap[trigger.route]} Uhr ausgeführt.`);
        });
      this.processTriggers();
    });
  }

  private processTriggers() {
    let currentTimeInMs = new Date().getTime(); // Aktuelle Zeit in Millisekunden

    Object.entries(this.triggerMap).forEach(([routine, triggerTime]) => {
      const timeDifference = triggerTime - currentTimeInMs; // Berechnung der Differenz in Millisekunden
      console.log(`Die Zeitdifferenz für ${routine} beträgt ${timeDifference}ms.`);

      // Wenn die Zeit in der Vergangenheit liegt (negative Differenz), nichts tun
      if (timeDifference < 0) {
        console.log(`Die Zeit für ${routine} liegt bereits in der Vergangenheit. Nichts tun.`);
      } else {
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
    // Cancel all active timeouts
    Object.entries(this.timoutReferenceMap).forEach(([routine, timeoutRef]) => {
      clearTimeout(timeoutRef);
      console.log(`Timeout für ${routine} wurde abgebrochen.`);
    });

    // Clear the timeout reference map
    this.timoutReferenceMap = {};
    console.log('Alle aktiven Timer wurden zurückgesetzt.');
  }
}
