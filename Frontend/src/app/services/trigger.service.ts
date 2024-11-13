import {Injectable} from '@angular/core';
import {ApiService} from './api.service';
import {ChatService} from './chat.service';
import {interval, timer} from 'rxjs';

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
  private isGloballyBlocked = false;

  constructor(
    private apiService: ApiService,
    private chatService: ChatService
  ) {this.setOffTrigger()}

  currentDate = new Date().toISOString().split('T')[0];

  setOffTrigger() {
    //this.prefferedMorningTime(); // Schauen und Setzen ob eine Morgenzeit in den Einstellungen gesetzt ist
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
    interval(1000).subscribe(() => { // Alle 60 Sekunden prüfen
      const currentTime = new Date().toString().slice(16, 21);// HH:mm Format
      console.log(`Aktuelle Zeit: ${currentTime}`);
      Object.entries(this.triggerTimes).forEach(([routine, time]) => {
        // Prüfen, ob die aktuelle Zeit mit der geplanten Zeit übereinstimmt und die Routine noch nicht blockiert ist
console.log(routine, time)
        if (currentTime >= time && !this.triggeredStatus[routine] && !this.isGloballyBlocked) {
          this.executeRoutine(routine);
          this.blockRoutine(routine); // Routine für eine Stunde blockieren
          this.setGlobalBlock(); // Alle Routinen für eine Stunde blockieren
          this.triggerTimes['/api/logic/mittag'] = '11:23'
        }
        if (currentTime === time && !this.triggeredStatus[routine]) {
          this.executeRoutine(routine);
          this.blockRoutine(routine); // Routine für eine Stunde blockieren
        }
      });
    });
  }

  private setGlobalBlock() {
    this.isGloballyBlocked = true; // Blockierung aktivieren
    console.log('Alle Routinen sind nun für eine Stunde blockiert.');

    // Timer, um die Blockierung nach einer Stunde wieder aufzuheben
    timer(3600000).subscribe(() => { // 3600000 ms = 1 Stunde
      this.isGloballyBlocked = false; // Blockierung aufheben
      console.log('Alle Routinen sind wieder freigegeben und können erneut ausgelöst werden.');
    });
  }


  private blockRoutine(routine: string) {
    this.triggeredStatus[routine] = true;
    setTimeout(() => {
      this.triggeredStatus[routine] = false;
    }, 3600000); // 1 Stunde in Millisekunden
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
