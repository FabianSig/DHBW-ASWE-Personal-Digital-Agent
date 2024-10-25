import {Component, EventEmitter, Output} from '@angular/core';
import {ApiService} from '../services/api.service';
import {MenuResponse} from '../interfaces/menu-response';

@Component({
  selector: 'app-menu',
  standalone: true,
  imports: [],
  templateUrl: './menu.component.html',
  styleUrl: './menu.component.scss'
})
export class MenuComponent {
  @Output() menuResponseEmitter = new EventEmitter<MenuResponse>();

  constructor(private apiService: ApiService) {}

  handleMenu(menuDate: HTMLInputElement) {
    this.apiService.getMenuData(menuDate.value).subscribe({
      next: (res) => {
        console.log(res);
        this.menuResponseEmitter.emit(res as MenuResponse);
      },
      error: (error) => {
        console.error('Error occurred:', error);
      }
    });
  }
}
