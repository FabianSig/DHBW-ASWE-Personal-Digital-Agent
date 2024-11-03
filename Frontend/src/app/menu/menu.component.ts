import {Component,} from '@angular/core';
import {ApiService} from '../services/api.service';
import {MenuResponse, Speise} from '../interfaces/menu-response';
import {MenuCarouselComponent} from '../menu-carousel/menu-carousel.component';

@Component({
  selector: 'app-menu',
  standalone: true,
  templateUrl: './menu.component.html',
  imports: [
    MenuCarouselComponent
  ],
  styleUrl: './menu.component.scss'
})
export class MenuComponent {
  menuResponse?: MenuResponse;
  constructor(private apiService: ApiService) {}

  handleMenu(menuDate: HTMLInputElement) {
    this.apiService.getMenuData(menuDate.value).subscribe({
      next: (res) => {
        this.menuResponse = res as MenuResponse;
      },
      error: (error) => {
        console.error('Error occurred:', error);
      }
    });
  }
}
