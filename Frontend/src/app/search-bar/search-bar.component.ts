import { Component } from '@angular/core';

@Component({
  selector: 'app-search-bar',
  standalone: true,
  imports: [],
  templateUrl: './search-bar.component.html',
  styleUrl: './search-bar.component.scss'
})
export class SearchBarComponent {
  searchTerm: string = '';

  onSearch(event: any): void {
    this.searchTerm = event.target.value;
  }

  sendDataToChatGPT(): void {
    return;
  }
}
