import {Component} from '@angular/core';
import {ApiService} from '../services/api.service';
import {MessageBoxComponent} from '../message-box/message-box.component';

@Component({
  selector: 'app-search-bar',
  standalone: true,
  imports: [],
  templateUrl: './search-bar.component.html',
  styleUrl: './search-bar.component.scss'
})
export class SearchBarComponent {

  searchTerm: string = '';
  error: string = '';

  constructor(private apiService: ApiService, private messageBoxComponent: MessageBoxComponent) {
  }

  onSearch(event: any): void {
    this.searchTerm = event.target.value;
  }

  handleChatGPTSearch(): void {
    this.apiService.getChatGPTData(this.searchTerm).subscribe(response => {
      this.messageBoxComponent.addGptMessage(response);
      this.apiService.chatGPTResponse.set(response);
      console.log(response);
    })
  }
}
