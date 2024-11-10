import {Injectable} from '@angular/core';
import {ApiService} from './api.service';
import {AlarmService} from './alarm.service';
import {ChatService} from './chat.service';
import {interval} from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class TriggerService {
  private triggerTimes: { [url: string]: number } = {};

  constructor(
    private apiService: ApiService,
    private alarmService: AlarmService,
    private chatService: ChatService
  ) {}
  currentDate = new Date().toISOString().split('T')[0];

  setOffTrigger() {
    this.apiService.getTriggerData("2024-11-8").subscribe((data: any) => {
      data.triggers.forEach((trigger: any) => {
        this.triggerTimes[trigger.url] = this.convertTimeToMs(trigger.time);
      });

      const alarmPreference = this.getAlarmFromPreferences();
      if (alarmPreference) {
        this.triggerTimes['/api/logic/morning'] = this.convertTimeToMs(alarmPreference);
      }
      this.setAlarms();
    });

    // Regelmäßig prüfen, ob ein Trigger erreicht wurde
    this.checkForTriggeredAlarms();
  }

  private setAlarms() {
    Object.entries(this.triggerTimes).forEach(([, time]) => {
      this.alarmService.setUpAlarm(time);
    });
  }

  private checkForTriggeredAlarms() {
    interval(60000).subscribe(() => {  // Prüfe jede Minute
      const currentTime = new Date().getTime();
      Object.entries(this.triggerTimes).forEach(([url, time]) => {
        if (currentTime >= time) {
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
        message = 'Morgenroutine gestartet!';
        break;
      case '/api/logic/mittag':
        message = 'Mittagsmenüvorschlag bereit!';
        break;
      case '/api/logic/abend':
        message = 'Heimfahrtsvorschlag bereit!';
        break;
    }
    this.chatService.addMessage(message, 'chatgpt');
  }
  private convertTimeToMs(timeString: string): number {
    const [hours, minutes] = timeString.split(':').map(Number);
    const now = new Date();
    now.setHours(hours, minutes, 0, 0);
    return now.getTime();
  }

  private getAlarmFromPreferences(): string | null {
    return localStorage.getItem('wecker-time');
  }
}
