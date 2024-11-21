package online.dhbw_studentprojekt.mschatgpt.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import online.dhbw_studentprojekt.mschatgpt.client.ChatGPTClient;
import online.dhbw_studentprojekt.mschatgpt.dto.audio.ChatGPTAudioRequest;
import online.dhbw_studentprojekt.mschatgpt.model.Message;
import online.dhbw_studentprojekt.mschatgpt.model.Prompt;
import online.dhbw_studentprojekt.mschatgpt.repository.MessageRepository;
import online.dhbw_studentprojekt.mschatgpt.repository.PromptRepository;
import online.dhbw_studentprojekt.dto.chatgpt.audio.ChatGPTAudioResponse;
import online.dhbw_studentprojekt.dto.chatgpt.intention.ChatGPTIntentionAttribute;
import online.dhbw_studentprojekt.dto.chatgpt.intention.ChatGPTIntentionResponse;
import online.dhbw_studentprojekt.dto.chatgpt.morning.MorningRequest;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatGPTMessage;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatGPTResponse;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatGPTResponseChoice;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatMessageRequest;
import online.dhbw_studentprojekt.dto.stock.Stock;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockMultipartFile;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;

@ExtendWith(MockitoExtension.class)
class ChatGPTServiceTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private PromptRepository promptRepository;

    @Mock
    private ChatGPTClient chatGPTClient;

    @InjectMocks
    private ChatGPTService chatGPTService;

    @Test
    void sendMessage_WithExtraPrompt_ShouldReturnResponseChoice() {
        // Arrange
        ChatMessageRequest request = new ChatMessageRequest("Test Message", "Test Data");
        String chatId = "default";
        String extraPromptId = "extraPrompt";

        Prompt prompt = new Prompt("1", "message", "system", "Default system prompt");
        Prompt extraPrompt = new Prompt("2", "extraPrompt", "system", "Extra system prompt");
        Message previousMessage = new Message("1", chatId, "user", "Previous message");

        List<Message> previousMessages = List.of(previousMessage);
        ChatGPTResponseChoice mockChoice = new ChatGPTResponseChoice(new ChatGPTMessage("assistant", "response content"));

        ChatGPTResponse mockResponse = new ChatGPTResponse(List.of(mockChoice));

        Mockito.when(promptRepository.findFirstByPromptId("message")).thenReturn(Optional.of(prompt));
        Mockito.when(promptRepository.findFirstByPromptId(extraPromptId)).thenReturn(Optional.of(extraPrompt));
        Mockito.when(messageRepository.findByChatId(chatId)).thenReturn(previousMessages);
        Mockito.when(chatGPTClient.sendMessage(Mockito.any())).thenReturn(mockResponse);

        // Act
        ChatGPTResponseChoice result = chatGPTService.sendMessage(request, chatId, extraPromptId);

        // Assert
        Assertions.assertEquals("assistant", result.message().role());
        Assertions.assertEquals("response content", result.message().content());

        // Verify interactions
        Mockito.verify(promptRepository).findFirstByPromptId("message");
        Mockito.verify(promptRepository).findFirstByPromptId(extraPromptId);
        Mockito.verify(messageRepository).findByChatId(chatId);
        Mockito.verify(chatGPTClient).sendMessage(Mockito.any());
    }

    @Test
    void sendAudio_ShouldReturnAudioResponse() {
        // Arrange
        MockMultipartFile mockFile = new MockMultipartFile("file", "audio.mp3", "audio/mpeg", "audio data".getBytes());
        ChatGPTAudioRequest request = new ChatGPTAudioRequest(mockFile, "whisper-1", "de");
        ChatGPTAudioResponse mockResponse = new ChatGPTAudioResponse("transcription");

        Mockito.when(chatGPTClient.sendAudio(mockFile, request.model(), request.language())).thenReturn(mockResponse);

        // Act
        ChatGPTAudioResponse result = chatGPTService.sendAudio(request);

        // Assert
        Assertions.assertEquals("transcription", result.text());

        // Verify interactions
        Mockito.verify(chatGPTClient).sendAudio(mockFile, request.model(), request.language());
    }
    @Test
    void findIntention_ShouldReturnIntentionResponse() throws Exception {
        // Arrange
        ChatGPTMessage message = new ChatGPTMessage("user", "Test Message");

        // Mock a prompt from the repository
        Prompt mockPrompt = new Prompt("1", "intent", "system", "intent prompt content");
        ObjectMapper objectMapper = new ObjectMapper();

        // Mock the intention attributes
        List<ChatGPTIntentionAttribute> mockAttributes = List.of(
                new ChatGPTIntentionAttribute("attribute1", "value1"),
                new ChatGPTIntentionAttribute("attribute2", "value2")
        );

        // Mock the intention response
        ChatGPTIntentionResponse mockIntentionResponse = new ChatGPTIntentionResponse("intention", mockAttributes);


        // Mock the ChatGPTResponse containing the ChatGPTIntentionResponse as JSON
        ChatGPTResponse mockResponse = new ChatGPTResponse(List.of(
                new ChatGPTResponseChoice(new ChatGPTMessage("assistant", objectMapper.writeValueAsString(mockIntentionResponse)))
        ));

        // Mock repository and client behavior
        Mockito.when(promptRepository.findByPromptId("intent")).thenReturn(List.of(mockPrompt));
        Mockito.when(chatGPTClient.sendIntentionMessage(Mockito.any())).thenReturn(mockResponse);

        // Act
        ChatGPTIntentionResponse result = chatGPTService.findIntention(message);

        // Assert
        Assertions.assertEquals("intention", result.route());
        Assertions.assertEquals(2, result.attributes().size());
        Assertions.assertEquals("attribute1", result.attributes().get(0).name());
        Assertions.assertEquals("value1", result.attributes().get(0).value());
        Assertions.assertEquals("attribute2", result.attributes().get(1).name());
        Assertions.assertEquals("value2", result.attributes().get(1).value());

        // Verify interactions
        Mockito.verify(promptRepository).findByPromptId("intent");
        Mockito.verify(chatGPTClient).sendIntentionMessage(Mockito.any());
    }

    @Test
    void getMorning_ShouldReturnResponseChoice() {
        // Arrange
        MorningRequest request = new MorningRequest(
                "First Headline",
                "Second Headline",
                "Third Headline",
                List.of(new Stock("AAPL", new Stock.DataPoint("2024-11-07", "150.0", "155.0", "149.0", "153.0"),
                        new Stock.DataPoint("2024-11-06", "145.0", "152.0", "144.0", "150.0"))));

        Prompt morningPrompt = new Prompt("1", "morning", "system", "Morning briefing: %s %s %s %s");

        ChatGPTResponseChoice mockChoice = new ChatGPTResponseChoice(new ChatGPTMessage("assistant", "Good morning! Here's your briefing."));
        ChatGPTResponse mockResponse = new ChatGPTResponse(List.of(mockChoice));

        Mockito.when(promptRepository.findFirstByPromptId("morning")).thenReturn(Optional.of(morningPrompt));
        Mockito.when(chatGPTClient.sendMessage(Mockito.any())).thenReturn(mockResponse);

        // Act
        ChatGPTResponseChoice result = chatGPTService.getMorning(request);

        // Assert
        Assertions.assertEquals("assistant", result.message().role());
        Assertions.assertEquals("Good morning! Here's your briefing.", result.message().content());

        // Verify interactions
        Mockito.verify(promptRepository).findFirstByPromptId("morning");
        Mockito.verify(chatGPTClient).sendMessage(Mockito.any());
    }

}
