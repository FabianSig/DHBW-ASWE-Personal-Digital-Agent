import { Component } from '@angular/core';
import {AlarmService} from '../services/alarm.service';

@Component({
  selector: 'app-alarm-clock',
  standalone: true,
  imports: [],
  templateUrl: './alarm-clock.component.html',
  styleUrl: './alarm-clock.component.scss'
})
export class AlarmClockComponent {
  constructor(private alarmService: AlarmService) {}

  setAlarm(alarmDateTime: HTMLInputElement) {
    if (alarmDateTime) {
      const alarmTime = new Date(alarmDateTime.value).getTime();
      const currentTime = new Date().getTime();

      if (alarmTime > currentTime) {
        this.alarmService.setUpAlarm(alarmTime);
        alert('Alarm set for: ' + new Date(alarmTime).toLocaleString());
      } else {
        alert('Please select a future date and time for the alarm.');
      }
    } else {
      alert('Please select a date and time for the alarm.');
    }
  }

  getAlarms(): string[] {
    return this.alarmService.getAlarms().map(time => new Date(time).toLocaleString());
  }
}
