import { TestBed, ComponentFixture } from '@angular/core/testing';
import { ChatInputComponent } from './chat-input.component';
import { ApiService } from '../services/api.service';
import { ChatService } from '../services/chat.service';
import { FormsModule } from '@angular/forms';
import { of } from 'rxjs';

describe('ChatInputComponent', () => {
  let component: ChatInputComponent;
  let fixture: ComponentFixture<ChatInputComponent>;
  let chatServiceMock: jasmine.SpyObj<ChatService>;
  let apiServiceMock: jasmine.SpyObj<ApiService>;

  beforeEach(async () => {
    // Create mock objects for ApiService and ChatService
    chatServiceMock = jasmine.createSpyObj('ChatService', ['addMessage']);
    apiServiceMock = jasmine.createSpyObj('ApiService', ['getChatGPTData', 'getTtsAudioFile']);

    await TestBed.configureTestingModule({
      imports: [FormsModule],
      providers: [
        { provide: ApiService, useValue: apiServiceMock },
        { provide: ChatService, useValue: chatServiceMock }
      ]
    }).compileComponents();

    fixture = TestBed.createComponent(ChatInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should update searchTerm on onSearch', () => {
    const mockEvent = { target: { value: 'Hello' } };
    component.onSearch(mockEvent);
    expect(component.searchTerm).toBe('Hello');
  });

  it('should call addMessage and ApiService on handleChatGPTSearch', () => {
    apiServiceMock.getChatGPTData.and.returnValue(of('ChatGPT Response'));
    apiServiceMock.getTtsAudioFile.and.returnValue(of(new Blob()));

    component.searchTerm = 'Test Input';
    component.handleChatGPTSearch();

    expect(chatServiceMock.addMessage).toHaveBeenCalledWith('Test Input', 'user');
    expect(apiServiceMock.getChatGPTData).toHaveBeenCalledWith('Test Input');
  });
});
