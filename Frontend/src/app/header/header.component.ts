import {Component, OnDestroy, OnInit} from '@angular/core';
import {CarouselComponent} from '../carousel/carousel.component';

@Component({
  selector: 'app-header',
  standalone: true,
  imports: [
    CarouselComponent
  ],
  templateUrl: './header.component.html',
  styleUrl: './header.component.scss'
})
export class HeaderComponent implements OnInit, OnDestroy {
  title = 'Personal-Digital-Agent-Frontend';
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
    this.day = dateTime.toISOString().split('T')[0];
    this.time = dateTime.toTimeString().split(' ')[0];
  }

}
