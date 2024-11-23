import { TestBed } from '@angular/core/testing';
import { ChatService } from './chat.service';
import { ChatMessage } from '../interfaces/chat-message';

describe('ChatService', () => {
  let service: ChatService;

  beforeEach(() => {
    TestBed.configureTestingModule({
      providers: [ChatService]
    });
    service = TestBed.inject(ChatService);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });

  it('should add new message from user and set isLoading to true', () => {
    const messageText = 'Hello!';
    const sender = 'user';

    service.addMessage(messageText, sender);

    const messages = service.getMessages();
    expect(messages.length).toBe(1);
    expect(messages[0]).toEqual(jasmine.objectContaining<ChatMessage>({ text: messageText, sender, id: 0 }));
    expect(service.isLoading).toBeTrue(); // isLoading should be true when user sends a message
  });

  it('should add new message from chatGPT and set isLoading to false', () => {
    const messageText = 'Hi, how can I help you?';
    const sender = 'chatgpt';

    service.addMessage(messageText, sender);

    const messages = service.getMessages();
    expect(messages.length).toBe(1);
    expect(messages[0]).toEqual(jasmine.objectContaining<ChatMessage>({ text: messageText, sender, id: 0 }));
    expect(service.isLoading).toBeFalse(); // isLoading should be false when chatGPT sends a response
  });
});
