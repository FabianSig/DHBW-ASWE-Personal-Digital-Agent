import {Component, OnDestroy, OnInit} from '@angular/core';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit, OnDestroy {
  title = 'Assistify AI';
  day = '';
  time = '';
  private timerId: any;

  ngOnInit() {
    this.updateDateTime();
    // Set up a timer to update the date and time every second in the header
    this.timerId = setInterval(() => {
      this.updateDateTime();
    }, 1000);
  }

  ngOnDestroy() {
    // Clear the timer to prevent memory leaks when the component is destroyed
    if (this.timerId) {
      clearInterval(this.timerId);
    }
  }

  private updateDateTime() {
    // Updates day and time for the header
    const dateTime = new Date();
    const day = String(dateTime.getDate()).padStart(2, '0');
    const month = String(dateTime.getMonth() + 1).padStart(2, '0'); // +1, because months fo from 0 to 11
    const year = dateTime.getFullYear();

    this.day = `${day}.${month}.${year}`;
    this.time = dateTime.toTimeString().split(' ')[0];
  }
}
