import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ChatInputComponent } from './chat-input.component';
import { FormsModule } from '@angular/forms';
import { AudioRecorderComponent } from '../audio-recorder/audio-recorder.component';
import { ApiService } from '../services/api.service';
import { ChatService } from '../services/chat.service';
import { of } from 'rxjs';
import { AudioResponse } from '../interfaces/audio-response';

describe('ChatInputComponent', () => {
  let component: ChatInputComponent;
  let fixture: ComponentFixture<ChatInputComponent>;
  let apiServiceSpy: jasmine.SpyObj<ApiService>;
  let chatServiceSpy: jasmine.SpyObj<ChatService>;

  beforeEach(async () => {
    const apiServiceMock = jasmine.createSpyObj('ApiService', ['getChatGPTData', 'getTtsAudioFile']);
    const chatServiceMock = jasmine.createSpyObj('ChatService', ['addMessage']);

    await TestBed.configureTestingModule({
      imports: [FormsModule, AudioRecorderComponent],
      providers: [
        { provide: ApiService, useValue: apiServiceMock },
        { provide: ChatService, useValue: chatServiceMock }
      ]
    })
    .compileComponents();

    apiServiceSpy = TestBed.inject(ApiService) as jasmine.SpyObj<ApiService>;
    chatServiceSpy = TestBed.inject(ChatService) as jasmine.SpyObj<ChatService>;

    fixture = TestBed.createComponent(ChatInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should update searchTerm on search', () => {
    const mockEvent = { target: { value: 'test search' } };
    component.onSearch(mockEvent);
    expect(component.searchTerm).toBe('test search');
  });

  it('should add user message and clear searchTerm after handleChatGPTSearch', () => {
    component.searchTerm = 'Hello GPT';
    const mockResponse = 'Hello from GPT';
    apiServiceSpy.getChatGPTData.and.returnValue(of(mockResponse));

    component.handleChatGPTSearch();

    expect(chatServiceSpy.addMessage).toHaveBeenCalledWith('Hello GPT', 'user');
    expect(apiServiceSpy.getChatGPTData).toHaveBeenCalledWith('Hello GPT');
    expect(chatServiceSpy.addMessage).toHaveBeenCalledWith(mockResponse, 'chatgpt');
    expect(component.searchTerm).toBe('');
  });

  it('should add user message from audio response and process it', () => {
    const audioResponse: AudioResponse = { text: 'Audio message content' };
    const mockResponse = 'Response from GPT';
    apiServiceSpy.getChatGPTData.and.returnValue(of(mockResponse));

    component.onAudioResponse(audioResponse);

    expect(chatServiceSpy.addMessage).toHaveBeenCalledWith('Audio message content', 'user');
    expect(apiServiceSpy.getChatGPTData).toHaveBeenCalledWith('Audio message content');
    expect(chatServiceSpy.addMessage).toHaveBeenCalledWith(mockResponse, 'chatgpt');
  });
});
