import {Injectable} from '@angular/core';
import {ApiService} from './api.service';
import {ChatService} from './chat.service';
import {interval} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TriggerService {
  private triggerTimes: { [url: string]: string } = {};
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
    this.prefferedMorningTime(); // Schauen und Setzen ob eine Morgenzeit in den Einstellungen gesetzt ist
    this.apiService.getTriggerData(this.currentDate).subscribe((data: any) => {
        data.triggers.map((trigger: any) => {
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
      console.warn('Keine Weckerzeit in den Präferenzen gefunden.');
    }
  }

  private checkForTriggeredTimes() {

    interval(2000).subscribe(() => { // Alle 60 Sekunden prüfen
      const currentTime = new Date().toString().slice(16, 21);// HH:mm Format
      const triggerTimes = {...this.triggerTimes};
      const intervals = [
        { routine: '/api/logic/morning', start: "00:00", end: triggerTimes['/api/logic/mittag'] },
        { routine: '/api/logic/mittag', start: triggerTimes['/api/logic/mittag'], end: triggerTimes['/api/logic/abend'] },
        { routine: '/api/logic/abend', start: triggerTimes['/api/logic/abend'], end: "22:00" }
      ];

      const currentInterval = intervals.find(
        ({ start, end }) => currentTime >= start && currentTime < end
      );

      if (currentInterval && !this.triggeredStatus[currentInterval.routine]) {
        this.executeRoutine(currentInterval.routine);
        this.blockRoutine(currentInterval.routine); //Routine blockieren, damit sie nicht mehrfach ausgeführt wird
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

  private formatTriggerTime(time: string): string {
    // Prüfen, ob die Zeit bereits im HH:mm-Format vorliegt
    if (!time.includes('T')) {
      return time; // Rückgabe, falls es bereits das richtige Format ist
    }
    const dateObj = new Date(time);
      const hours = dateObj.getHours().toString().padStart(2, '0');
      const minutes = dateObj.getMinutes().toString().padStart(2, '0');
      return `${hours}:${minutes}`; //Rückgabe im HH:mm-Format

  }
}
