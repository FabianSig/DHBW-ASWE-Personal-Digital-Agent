import { ComponentFixture, TestBed } from '@angular/core/testing';
import { MessageBoxComponent } from './message-box.component';
import { ChatService } from '../services/chat.service';
import { ChatMessage } from '../interfaces/chat-message';
import { MarkdownModule, MarkdownService } from 'ngx-markdown';
import {ElementRef, signal} from '@angular/core';

describe('MessageBoxComponent', () => {
  let component: MessageBoxComponent;
  let fixture: ComponentFixture<MessageBoxComponent>;
  let chatServiceSpy: jasmine.SpyObj<ChatService>;

  beforeEach(async () => {
    const chatServiceMock = jasmine.createSpyObj('ChatService', ['getMessages'], { isLoading: signal(false), });

    await TestBed.configureTestingModule({
      imports: [MarkdownModule.forRoot(), MessageBoxComponent], // MessageBoxComponent hier importieren
      providers: [
        { provide: ChatService, useValue: chatServiceMock },
        MarkdownService
      ]
    }).compileComponents();

    chatServiceSpy = TestBed.inject(ChatService) as jasmine.SpyObj<ChatService>;

    fixture = TestBed.createComponent(MessageBoxComponent);
    component = fixture.componentInstance;

    // Mock chat messages
    chatServiceSpy.getMessages.and.returnValue([{ id: 1, text: 'Hello World', sender: 'user' }] as ChatMessage[]);
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });

  it('should initialize with messages from chat service', () => {
    expect(component.messages.length).toBe(1);
    expect(component.messages[0].text).toBe('Hello World');
  });

  it('should update prevMessagesLength on ngAfterViewChecked', () => {
    component.ngAfterViewChecked();
    expect(component.prevMessagesLength).toBe(component.messages.length);
  });

  it('should scroll to bottom on new message', (done) => {
    // @ts-ignore
    component.scrollAnchor = {
      nativeElement: {
        scrollIntoView: jasmine.createSpy('scrollIntoView')
      }
    } as ElementRef;

    // Add a new message to trigger scrolling
    component.messages.push({ id: 2, text: 'New message', sender: 'user' });

    // Call ngAfterViewChecked, which includes the scrolling logic
    component.ngAfterViewChecked();

    // Wait for the setTimeout in scrollToBottom
    setTimeout(() => {
      // Verify scrollIntoView was called
      // @ts-ignore
      expect(component.scrollAnchor.nativeElement.scrollIntoView).toHaveBeenCalledWith({ behavior: 'smooth' });
      done();
    }, 600);
  });

  it('should not scroll to bottom if no new message is added', () => {
    const spy = jasmine.createSpy('scrollIntoView');
    // @ts-ignore
    component.scrollAnchor = {
      nativeElement: {
        scrollIntoView: spy
      }
    } as ElementRef;

    component.ngAfterViewChecked();  // No new message, so no scroll
    expect(spy).not.toHaveBeenCalled();
  });

  it('should handle undefined scrollAnchor', (done) => {
    // @ts-ignore
    component.scrollAnchor = undefined;
    spyOn<any>(component, 'scrollToBottom').and.callThrough();

    component.scrollToBottom();
    setTimeout(() => {
      expect(component['scrollToBottom']).toHaveBeenCalled();
      done();
    }, 600); // allows time for setTimeout to execute
  });
});
