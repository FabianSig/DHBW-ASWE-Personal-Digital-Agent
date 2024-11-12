import {Injectable} from '@angular/core';
import {ApiService} from './api.service';
import {ChatService} from './chat.service';
import {interval} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TriggerService {
  private triggerTimes: { [url: string]: Date } = {};

  constructor(
    private apiService: ApiService,
    private chatService: ChatService
  ) {}
  currentDate = new Date().toISOString().split('T')[0];

  setOffTrigger() {
    this.apiService.getTriggerData(this.currentDate).subscribe((data: any) => {
        data.triggers.forEach((trigger: any) => {
          this.triggerTimes[trigger.route] = new Date(trigger.time);
        });
     });
    this.prefferedMorningTime(); // Schauen und Setzen ob eine Morgenzeit in den Einstellungen gesetzt ist
    this.checkForTriggeredTimes(); // Prüfe jede Minute, ob ein Alarm ausgelöst werden soll
  }

 private prefferedMorningTime() {
  const morningTime = this.getAlarmFromPreferences()
    if (morningTime) {
      const [hours, minutes] = morningTime.split(':').map(Number);
      if (hours < 10) {
        const now = new Date();
        now.setHours(hours, minutes, 0, 0);

        this.triggerTimes['/api/logic/morning'] = now;
      } else {
        console.warn('Die eingestellte Weckerzeit ist nicht vor 10 Uhr morgens und wird daher ignoriert.');
      }
    } else {
      console.warn('Keine Weckerzeit in den Präferenzen gefunden.');
    }
  }


  private checkForTriggeredTimes() {
    interval(60000).subscribe(() => {  // Prüfe jede Minute
      const currentTime = new Date().getTime();// Aktuelle Zeit als Millisekundenwert
      Object.entries(this.triggerTimes).forEach(([url, time]) => {
        const triggerTimeMs = (time as Date).getTime();  // Triggerzeit in Millisekunden

        if (currentTime >= triggerTimeMs) { //Millisekunden verlgleich für übersichtlichkeit
          this.executeRoutine(url);
          delete this.triggerTimes[url];  // Trigger nur einmal auslösen
        }
      });
    });
  }
  private executeRoutine(url: string) {
    let message = '';

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
        message = 'TODO - Abendroutine';
        this.chatService.addMessage(message, 'chatgpt');
        break;
      default:
        console.warn(`Unbekannte URL: ${url}`);
    }
  }

  private getAlarmFromPreferences(): string | null {
    return localStorage.getItem('Weckerzeit');
  }
}
