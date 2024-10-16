import { Component } from '@angular/core';
import { RouterOutlet } from '@angular/router';
import {SearchBarComponent} from './search-bar/search-bar.component';

@Component({
  selector: 'app-root',
  standalone: true,
  imports: [RouterOutlet, SearchBarComponent],
  templateUrl: './app.component.html',
  styleUrl: './app.component.scss'
})
export class AppComponent {
  title = 'Personal-Digital-Agent-Frontend';
}
