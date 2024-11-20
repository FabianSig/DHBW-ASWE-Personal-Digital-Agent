package nikomitk.mschatgpt.controller;

import nikomitk.mschatgpt.dto.audio.ChatGPTAudioRequest;
import nikomitk.mschatgpt.service.ChatGPTService;
import online.dhbw_studentprojekt.dto.chatgpt.audio.ChatGPTAudioResponse;
import online.dhbw_studentprojekt.dto.chatgpt.intention.ChatGPTIntentionAttribute;
import online.dhbw_studentprojekt.dto.chatgpt.intention.ChatGPTIntentionResponse;
import online.dhbw_studentprojekt.dto.chatgpt.morning.MorningRequest;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatGPTMessage;
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

import org.junit.jupiter.api.Assertions;

@ExtendWith(MockitoExtension.class)
class ChatGPTControllerTest {

    @Mock
    private ChatGPTService chatGPTService;

    @InjectMocks
    private ChatGPTController chatGPTController;

    @Test
    void sendMessage_WithChatId_ShouldReturnResponseChoice() {
        // Arrange
        String chatId = "testChatId";
        String extraPromptId = "testExtraPrompt";
        ChatMessageRequest request = new ChatMessageRequest("Test Message", "Test Data");

        ChatGPTResponseChoice mockResponse = new ChatGPTResponseChoice(new ChatGPTMessage("assistant", "response content"));
        Mockito.when(chatGPTService.sendMessage(request, chatId, extraPromptId)).thenReturn(mockResponse);

        // Act
        ChatGPTResponseChoice result = chatGPTController.sendMessage(request, chatId, extraPromptId);

        // Assert
        Assertions.assertEquals("assistant", result.message().role());
        Assertions.assertEquals("response content", result.message().content());

        Mockito.verify(chatGPTService).sendMessage(request, chatId, extraPromptId);
    }

    @Test
    void sendMessage_WithoutChatId_ShouldReturnResponseChoice() {
        // Arrange
        String extraPromptId = "testExtraPrompt";
        ChatMessageRequest request = new ChatMessageRequest("Test Message", "Test Data");

        ChatGPTResponseChoice mockResponse = new ChatGPTResponseChoice(new ChatGPTMessage("assistant", "response content"));
        Mockito.when(chatGPTService.sendMessage(request, extraPromptId)).thenReturn(mockResponse);

        // Act
        ChatGPTResponseChoice result = chatGPTController.sendMessage(request, extraPromptId);

        // Assert
        Assertions.assertEquals("assistant", result.message().role());
        Assertions.assertEquals("response content", result.message().content());

        Mockito.verify(chatGPTService).sendMessage(request, extraPromptId);
    }

    @Test
    void sendAudio_ShouldReturnAudioResponse() {
        // Arrange
        MockMultipartFile mockFile = new MockMultipartFile("file", "audio.mp3", "audio/mpeg", "audio data".getBytes());
        ChatGPTAudioResponse mockResponse = new ChatGPTAudioResponse("audio-transcription");

        Mockito.when(chatGPTService.sendAudio(Mockito.any(ChatGPTAudioRequest.class))).thenReturn(mockResponse);

        // Act
        ChatGPTAudioResponse result = chatGPTController.sendAudio(mockFile);

        // Assert
        Assertions.assertEquals("audio-transcription", result.text());
        Mockito.verify(chatGPTService).sendAudio(Mockito.any(ChatGPTAudioRequest.class));
    }

    @Test
    void findIntention_ShouldReturnIntentionResponse() {
        // Arrange
        String message = "What is my intention?";
        ChatGPTIntentionResponse mockResponse = new ChatGPTIntentionResponse("intention",
                List.of(new ChatGPTIntentionAttribute("attribute", "value")));

        ChatGPTMessage chatGPTMessage = new ChatGPTMessage("user", message);
        Mockito.when(chatGPTService.findIntention(chatGPTMessage)).thenReturn(mockResponse);

        // Act
        ChatGPTIntentionResponse result = chatGPTController.findIntention(message);

        // Assert
        Assertions.assertEquals("intention", result.route());
        Assertions.assertEquals(1, result.attributes().size());
        Assertions.assertEquals("attribute", result.attributes().get(0).name());
        Assertions.assertEquals("value", result.attributes().get(0).value());

        Mockito.verify(chatGPTService).findIntention(chatGPTMessage);
    }

    @Test
    void getMorning_ShouldReturnMorningResponse() {
        // Arrange
        MorningRequest request = new MorningRequest(
                "First Headline",
                "Second Headline",
                "Third Headline",
                List.of(new Stock("AAPL",
                        new Stock.DataPoint("2024-11-07", "150.0", "155.0", "149.0", "153.0"),
                        new Stock.DataPoint("2024-11-06", "145.0", "152.0", "144.0", "150.0"))));

        ChatGPTResponseChoice mockResponse = new ChatGPTResponseChoice(new ChatGPTMessage("assistant", "Good morning! Here's your briefing."));
        Mockito.when(chatGPTService.getMorning(request)).thenReturn(mockResponse);

        // Act
        ChatGPTResponseChoice result = chatGPTController.getMorning(request);

        // Assert
        Assertions.assertEquals("assistant", result.message().role());
        Assertions.assertEquals("Good morning! Here's your briefing.", result.message().content());

        Mockito.verify(chatGPTService).getMorning(request);
    }
}
