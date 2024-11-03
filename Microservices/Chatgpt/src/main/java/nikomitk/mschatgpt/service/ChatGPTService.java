package nikomitk.mschatgpt.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nikomitk.mschatgpt.client.ChatGPTClient;
import nikomitk.mschatgpt.model.Prompt;
import online.dhbw_studentprojekt.dto.chatgpt.audio.ChatGPTAudioResponse;
import nikomitk.mschatgpt.dto.audio.ChatGPTAudioRequest;
import online.dhbw_studentprojekt.dto.chatgpt.intention.ChatGPTIntentionRequest;
import online.dhbw_studentprojekt.dto.chatgpt.intention.ChatGPTIntentionResponse;
import online.dhbw_studentprojekt.dto.chatgpt.morning.MorningRequest;
import online.dhbw_studentprojekt.dto.chatgpt.standard.*;
import online.dhbw_studentprojekt.dto.stock.Stock;
import nikomitk.mschatgpt.model.Message;
import nikomitk.mschatgpt.repository.MessageRepository;
import nikomitk.mschatgpt.repository.PromptRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatGPTService {


    private final MessageRepository messageRepository;
    private final PromptRepository promptRepository;
    private final ChatGPTClient chatGPTClient;

    public ChatGPTResponseChoice sendMessage(ChatMessageRequest request, String chatId) {

        ChatGPTMessage prompt = promptRepository.findByPromptId("message").stream()
                .map(m -> new ChatGPTMessage(m.getRole(), m.getContent()))
                .toList().getFirst();

        List<ChatGPTMessage> messages = new ArrayList<>(messageRepository.findByChatId(chatId).stream()
                .map(m -> new ChatGPTMessage(m.getRole(), m.getContent()))
                .toList());

        messages.addFirst(prompt);
        messages.add(1, new ChatGPTMessage("system", request.data()));

        Message newMessage = Message.builder()
                .role("user")
                .content(request.message())
                .chatId(chatId)
                .build();

        messages.add(new ChatGPTMessage(newMessage.getRole(), newMessage.getContent()));

        ChatGPTRequest chatGPTRequest = new ChatGPTRequest("gpt-4o-mini", messages);
        ChatGPTResponse response = chatGPTClient.sendMessage(chatGPTRequest);
        String chatGPTResponseMessage = response.choices().getFirst().message().content();

        Message responseMessage = Message.builder()
                .role("assistant")
                .content(chatGPTResponseMessage)
                .chatId(chatId)
                .build();

        messageRepository.save(newMessage);
        messageRepository.save(responseMessage);

        return response.choices().getFirst();
    }

    public ChatGPTResponseChoice sendMessage(ChatMessageRequest request) {
        String defaultChatId = "default";
        return sendMessage(request, defaultChatId);
    }

    public ChatGPTAudioResponse sendAudio(ChatGPTAudioRequest request) {

        return chatGPTClient.sendAudio(request.file(), request.model(), request.language());
    }

    public ChatGPTIntentionResponse findIntention(ChatGPTMessage message) {

        LocalDate currentDate = LocalDate.now();
        LocalTime currentTime = LocalTime.now();

        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

        List<ChatGPTMessage> messages = new ArrayList<>(promptRepository.findByPromptId("intent").stream()
                .map(m -> new ChatGPTMessage(m.getRole(), String.format(m.getContent(), currentDate.format(dateFormatter), currentTime.format(timeFormatter))))
                .toList());

        messages.add(message);

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> responseFormat;

        try {
            responseFormat = objectMapper.readValue(
                    getClass().getClassLoader().getResourceAsStream("responseFormat.json"),
                    new TypeReference<>() {}
            );
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading response format", e);
        }

        ChatGPTIntentionRequest chatGPTRequest = new ChatGPTIntentionRequest("gpt-4o", messages, responseFormat);

        try {
            ChatGPTResponse strResponse = chatGPTClient.sendIntentionMessage(chatGPTRequest);
            return objectMapper.readValue(strResponse.choices().getFirst().message().content(), ChatGPTIntentionResponse.class);
        }
        catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading response format", e);
        }

    }

    public ChatGPTResponseChoice getMorning(MorningRequest request) {

        Prompt prompt = promptRepository.findFirstByPromptId("morning");

        String content = String.format(prompt.getContent(), request.firstHeadline(), request.secondHeadline(), request.thirdHeadline(), request.stocks().stream().map(Stock::toString).toList());
        ChatGPTMessage promptMessage = new ChatGPTMessage(prompt.getRole(), content);

        List<ChatGPTMessage> messages = List.of(promptMessage);
        ChatGPTRequest chatGPTRequest = new ChatGPTRequest("gpt-4o-mini", messages);
        ChatGPTResponse response = chatGPTClient.sendMessage(chatGPTRequest);

        return response.choices().getFirst();

    }

}
