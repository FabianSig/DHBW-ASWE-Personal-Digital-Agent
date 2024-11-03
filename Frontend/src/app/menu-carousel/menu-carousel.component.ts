import { Component, Input } from '@angular/core';
import { CarouselModule } from 'ngx-bootstrap/carousel';
import {MenuResponse, Speise} from '../interfaces/menu-response';
import {NgForOf} from '@angular/common';



@Component({
  selector: 'app-menu-carousel',
  standalone: true,
  imports: [CarouselModule, NgForOf],
  templateUrl: './menu-carousel.component.html',
  styleUrls: ['./menu-carousel.component.scss']
})
export class MenuCarouselComponent {
  categories = [
    { title: 'Vorspeisen', key: 'vorspeisen' },
    { title: 'Veganer Renner', key: 'veganerRenner' },
    { title: 'Hauptgericht', key: 'hauptgericht' },
    { title: 'Beilagen', key: 'beilagen' },
    { title: 'Salat', key: 'salat' },
    { title: 'Dessert', key: 'dessert' },
    { title: 'Buffet', key: 'buffet' },
  ];
 @Input() menuResponse?: MenuResponse;

  getCategoryItems(categoryKey: string): Speise[] {
    return this.menuResponse ? this.menuResponse[categoryKey as keyof MenuResponse] || [] : [];
  }

}
