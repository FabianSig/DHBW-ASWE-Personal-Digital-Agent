import {Injectable} from '@angular/core';
import {ApiService} from './api.service';
import {ChatService} from './chat.service';
import {interval} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TriggerService {
  private triggerTimes: { [url: string]: String } = {};

  constructor(
    private apiService: ApiService,
    private chatService: ChatService
  ) {}
  currentDate = new Date().toISOString().split('T')[0];

  setOffTrigger() {
    this.prefferedMorningTime(); // Schauen und Setzen ob eine Morgenzeit in den Einstellungen gesetzt ist
    this.apiService.getTriggerData(this.currentDate).subscribe((data: any) => {
        data.triggers.forEach((trigger: any) => {
          this.triggerTimes[trigger.route] = this.formatTriggerTime(trigger.time);
        });
     });
    this.checkForTriggeredTimes(); // Prüfe jede Minute, ob ein Alarm ausgelöst werden soll
  }

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
      console.log(this.triggerTimes)
      console.warn('Keine Weckerzeit in den Präferenzen gefunden.');
    }
  }

  private checkForTriggeredTimes() {
    let routine = '';
    interval(1000).subscribe(() => {  // Prüfe jede Minute
      const currentTime = new Date().toString();
      Object.entries(this.triggerTimes).forEach(([url, time]) => {
        if (currentTime >= time) { //Millisekunden verlgleich für übersichtlichkeit
           routine = url
          this.executeRoutine(routine);
           delete this.triggerTimes[url];
        }
      });
    });
    return routine;
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

  private formatTriggerTime(time: string): string {
    // Prüfen, ob die Zeit bereits im HH:mm-Format vorliegt
    if (!time.includes('T')) {
      return time; // Rückgabe, falls es bereits das richtige Format ist
    }

    // Wenn die Zeit im ISO-Format vorliegt, konvertiere in HH:mm
    const dateObj = new Date(time);
    if (!isNaN(dateObj.getTime())) {
      const hours = dateObj.getHours().toString().padStart(2, '0');
      const minutes = dateObj.getMinutes().toString().padStart(2, '0');
      return `${hours}:${minutes}`;
    } else {
      console.warn(`Ungültiges Datum für Trigger: ${time}`);
      return "24:00"; // Fallback-Zeit bei ungültigen Daten
    }
  }
}
