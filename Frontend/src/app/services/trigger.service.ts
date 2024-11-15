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
/*
 private prefferedMorningTime() {
  const morningTime = this.getAlarmFromPreferences()
    if (morningTime) {
      let [hours, minutes] = morningTime.split(':').map(Number);
      (hours === undefined)?hours = 0:0;
      if (hours < 10) {
        this.triggerTimes['/api/logic/morning'] = `${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}`;
      } else {
        console.warn('Die eingestellte Weckerzeit ist nicht vor 10 Uhr morgens und wird daher ignoriert.');
      }
    } else {
      console.warn('Keine Weckerzeit in den Präferenzen gefunden.');
    }
  }
*/
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
    switch (url) {
      case '/api/logic/morning':
        this.apiService.getMorningRoutine().subscribe((response: string) => {
            this.chatService.addMessage(response, 'chatgpt');
          });
        break;
      case '/api/logic/mittag':
        this.apiService.getMittagRoutine().subscribe((response: string) => {
          this.chatService.addMessage(response, 'chatgpt');
        });
        break;
      case '/api/logic/abend':
        const message = 'TODO - Abendroutine';
        this.chatService.addMessage(message, 'chatgpt');
        break;
      default:
        console.warn(`Unbekannte URL: ${url}`);
    }
  }

  private getAlarmFromPreferences(): string | null {
    return localStorage.getItem('Weckerzeit');
  }

  private blockRoutine(routine: string) {
    this.triggeredStatus[routine] = true;
  }


}
