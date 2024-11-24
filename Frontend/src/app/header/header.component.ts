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
    this.timerId = setInterval(() => {
      this.updateDateTime();
    }, 1000);
  }

  ngOnDestroy() {
    if (this.timerId) {
      clearInterval(this.timerId);
    }
  }

  private updateDateTime() {
    const dateTime = new Date();
    const day = String(dateTime.getDate()).padStart(2, '0');
    const month = String(dateTime.getMonth() + 1).padStart(2, '0'); // +1, because months fo from 0 to 11
    const year = dateTime.getFullYear();

    this.day = `${day}.${month}.${year}`;
    this.time = dateTime.toTimeString().split(' ')[0];
  }

}
