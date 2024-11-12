package nikomitk.mschatgpt.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nikomitk.mschatgpt.client.ChatGPTClient;
import nikomitk.mschatgpt.dto.audio.ChatGPTAudioRequest;
import nikomitk.mschatgpt.model.Message;
import nikomitk.mschatgpt.model.Prompt;
import nikomitk.mschatgpt.repository.MessageRepository;
import nikomitk.mschatgpt.repository.PromptRepository;
import online.dhbw_studentprojekt.dto.chatgpt.audio.ChatGPTAudioResponse;
import online.dhbw_studentprojekt.dto.chatgpt.intention.ChatGPTIntentionRequest;
import online.dhbw_studentprojekt.dto.chatgpt.intention.ChatGPTIntentionResponse;
import online.dhbw_studentprojekt.dto.chatgpt.morning.MorningRequest;
import online.dhbw_studentprojekt.dto.chatgpt.standard.*;
import online.dhbw_studentprojekt.dto.stock.Stock;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatGPTService {


    private static final String GPT_MODEL = "gpt-4o-mini";
    private final MessageRepository messageRepository;
    private final PromptRepository promptRepository;
    private final ChatGPTClient chatGPTClient;

    public ChatGPTResponseChoice sendMessage(ChatMessageRequest request, String extraPromptId) {

        String defaultChatId = "default";
        return sendMessage(request, defaultChatId, extraPromptId);
    }

    public ChatGPTResponseChoice sendMessage(ChatMessageRequest request, String chatId, String extraPromptId) {

        ChatGPTMessage prompt = promptRepository.findFirstByPromptId("message")
                .map(m -> new ChatGPTMessage(m.getRole(), m.getContent()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Prompt not found"));

        List<ChatGPTMessage> messages = messageRepository.findByChatId(chatId).stream()
                .map(m -> new ChatGPTMessage(m.getRole(), m.getContent()))
                .collect(Collectors.collectingAndThen(Collectors.toList(), list ->
                        list.size() > 3 ? list.subList(list.size() - 3, list.size()) : list
                ));

        messages.addFirst(prompt);
        messages.add(1, new ChatGPTMessage("system", request.data()));

        if (StringUtils.hasText(extraPromptId)) {
            promptRepository.findFirstByPromptId(extraPromptId)
                    .map(m -> new ChatGPTMessage(m.getRole(), m.getContent()))
                    .ifPresent(m -> messages.add(1, m));
        }

        Message newMessage = Message.builder()
                .role("user")
                .content(request.message())
                .chatId(chatId)
                .build();

        messages.add(new ChatGPTMessage(newMessage.getRole(), newMessage.getContent()));

        ChatGPTRequest chatGPTRequest = new ChatGPTRequest(GPT_MODEL, messages);
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

    public ChatGPTAudioResponse sendAudio(ChatGPTAudioRequest request) {

        return chatGPTClient.sendAudio(request.file(), request.model(), request.language());
    }

    public ChatGPTIntentionResponse findIntention(ChatGPTMessage message) {

        final String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        final String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        final List<ChatGPTMessage> messages = new ArrayList<>(promptRepository.findByPromptId("intent").stream()
                .map(m -> new ChatGPTMessage(m.getRole(), String.format(m.getContent(), currentDate, currentTime)))
                .toList());

        messages.add(message);

        final ObjectMapper objectMapper = new ObjectMapper();
        final Map<String, Object> responseFormat;

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("responseFormat.json")) {
            responseFormat = objectMapper.readValue(is, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading response format", e);
        }

        final ChatGPTIntentionRequest chatGPTRequest = new ChatGPTIntentionRequest(GPT_MODEL, messages, responseFormat);

        try {
            String strResponse = chatGPTClient.sendIntentionMessage(chatGPTRequest).choices().getFirst().message().content();
            return objectMapper.readValue(strResponse, ChatGPTIntentionResponse.class);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading response format", e);
        }

    }

    public ChatGPTResponseChoice getMorning(MorningRequest request) {

        final Prompt prompt = promptRepository.findFirstByPromptId("morning")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Prompt not found"));

        final String content = String.format(prompt.getContent(), request.firstHeadline(), request.secondHeadline(), request.thirdHeadline(), request.stocks().stream().map(Stock::toString).toList());
        final ChatGPTMessage promptMessage = new ChatGPTMessage(prompt.getRole(), content);

        final ChatGPTRequest chatGPTRequest = new ChatGPTRequest(GPT_MODEL, List.of(promptMessage));
        ChatGPTResponse response = chatGPTClient.sendMessage(chatGPTRequest);

        return response.choices().getFirst();
    }

}
