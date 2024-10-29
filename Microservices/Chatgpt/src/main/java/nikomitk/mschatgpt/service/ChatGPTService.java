package nikomitk.mschatgpt.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import nikomitk.mschatgpt.client.ChatGPTClient;
import nikomitk.mschatgpt.dto.audio.ChatGPTAudioResponse;
import nikomitk.mschatgpt.dto.audio.ChatGPTAudioRequest;
import nikomitk.mschatgpt.dto.intention.ChatGPTIntentionRequest;
import nikomitk.mschatgpt.dto.intention.ChatGPTIntentionResponse;
import nikomitk.mschatgpt.dto.standard.*;
import nikomitk.mschatgpt.model.Message;
import nikomitk.mschatgpt.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatGPTService {

    private static final Logger log = LoggerFactory.getLogger(ChatGPTService.class);
    private final MessageRepository messageRepository;
    private final ChatGPTClient chatGPTClient;

    public ChatGPTResponseChoice sendMessage(MessageRequest request, String chatId) {



        List<ChatGPTMessage> messages = new ArrayList<>(messageRepository.findByChatId(chatId).stream()
                .map(m -> {
                    log.info("m.getRole() = {}###", m.getRole());
                    log.info("m.getContent() = {}###", m.getContent());
                    return new ChatGPTMessage(m.getRole(), m.getContent());

                })
                .toList());


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

    public ChatGPTResponseChoice sendMessage(MessageRequest request) {
        String defaultChatId = "default";
        return sendMessage(request, defaultChatId);
    }

    public ChatGPTAudioResponse sendAudio(ChatGPTAudioRequest request) {
        return chatGPTClient.sendAudio(request.file(), request.model(), request.language());
    }

    public ChatGPTIntentionResponse findIntention(ChatGPTMessage message) {
        String intentChatId = "intent";

        List<ChatGPTMessage> messages = new ArrayList<>(messageRepository.findByChatId(intentChatId).stream()
                .map(m -> new ChatGPTMessage(m.getRole(), m.getContent()))
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
}
