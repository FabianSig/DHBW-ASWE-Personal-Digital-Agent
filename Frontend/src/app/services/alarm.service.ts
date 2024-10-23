import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class AlarmService {
  private alarms: number[] = [];

  constructor() {}

  setUpAlarm(alarmTime: number) {
    this.alarms.push(alarmTime);
    this.scheduleAlarm(alarmTime);
  }

  getAlarms(): number[] {
    return this.alarms;
  }

  private scheduleAlarm(alarmTime: number) {
    const currentTime = new Date().getTime();
    const timeDifference = alarmTime - currentTime;

    setTimeout(() => {
      alert('ALAARM!');
      this.alarms = this.alarms.filter(time => time !== alarmTime);
    }, timeDifference);
  }
}
