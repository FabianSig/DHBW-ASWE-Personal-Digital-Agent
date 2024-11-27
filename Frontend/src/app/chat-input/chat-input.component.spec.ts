import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ChatInputComponent } from './chat-input.component';
import { FormsModule } from '@angular/forms';
import { AudioRecorderComponent } from '../audio-recorder/audio-recorder.component';
import { ApiService } from '../services/api.service';
import { ChatService } from '../services/chat.service';

describe('ChatInputComponent', () => {
  let component: ChatInputComponent;
  let fixture: ComponentFixture<ChatInputComponent>;

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

    fixture = TestBed.createComponent(ChatInputComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
