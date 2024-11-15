import {Injectable} from '@angular/core';
import {ApiService} from './api.service';
import {ChatService} from './chat.service';
import {interval} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TriggerService {
  private triggerTimes: { [url: string]: number } = {};
  private triggeredStatus: { [key: string]: boolean } = {
    morning: false,
    mittag: false,
    abend: false
  };

  constructor(
    private apiService: ApiService,
    private chatService: ChatService
  ) {this.setOffTrigger()}

  currentDate = new Date().toISOString().split('T')[0];

  setOffTrigger() {
  //  this.prefferedMorningTime(); // Schauen und Setzen ob eine Morgenzeit in den Einstellungen gesetzt ist
    this.apiService.getTriggerData(this.currentDate).subscribe((data: any) => {
        data.triggers.map((trigger: any) => {
          this.triggerTimes[trigger.route] = new Date(trigger.time).getTime();
          console.log(`Routine ${trigger.route} wird um ${this.triggerTimes[trigger.route]} Uhr ausgeführt.`);
        });
      this.checkForTriggeredTimes(); // Prüfe jede Minute, ob ein Alarm ausgelöst werden soll
    });
  }

  private checkForTriggeredTimes() {
    let currentTimeInMs = new Date().getTime(); // Aktuelle Zeit in Millisekunden
        currentTimeInMs = new Date().setHours(15, 44, 0,0);


    Object.entries(this.triggerTimes).forEach(([routine, triggerTime]) => {
      const timeDifference = triggerTime - currentTimeInMs; // Berechnung der Differenz in Millisekunden
      console.log(`Die Zeitdifferenz für ${routine} beträgt ${timeDifference}ms.`);


      // Wenn die Zeit in der Vergangenheit liegt (negative Differenz), nichts tun
      if (timeDifference < 0) {
        console.log(`Die Zeit für ${routine} liegt bereits in der Vergangenheit. Nichts tun.`);
      } else {
        // Wenn die Zeit in der Zukunft liegt (positive Differenz), Timer setzen
        setTimeout(() => {
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

  private getAlarmFromPreferences(): string | null {
    return localStorage.getItem('Weckerzeit');
  }

  private blockRoutine(routine: string) {
    this.triggeredStatus[routine] = true;
  }


}
