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
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatGPTService {

    private final MessageRepository messageRepository;
    private final ChatGPTClient chatGPTClient;
    private static final Logger logger = LoggerFactory.getLogger(ChatGPTService.class);

    public ChatGPTResponseChoice<String> sendMessage(MessageRequest request, String chatId) {
        logger.info("Sending message with chatId: {}", chatId);

        List<ChatGPTMessage<String>> messages = new java.util.ArrayList<>(messageRepository.findByChatId(chatId).stream()
                .map(m -> {
                    logger.debug("Fetched message from repository with role: {}, content: {}", m.getRole(), m.getContent());
                    return new ChatGPTMessage<>(m.getRole(), m.getContent());
                })
                .toList());

        Message newMessage = Message.builder()
                .role("user")
                .content(request.message())
                .chatId(chatId)
                .build();

        messages.add(new ChatGPTMessage<>(newMessage.getRole(), newMessage.getContent()));
        logger.info("Added new user message to messages list with content: {}", newMessage.getContent());

        ChatGPTRequest chatGPTRequest = new ChatGPTRequest("gpt-4o", messages);
        logger.debug("Sending request to ChatGPTClient with model: {}, message count: {}", chatGPTRequest.getModel(), messages.size());

        ChatGPTResponse<String> response = chatGPTClient.sendMessage(chatGPTRequest);
        String chatGPTResponseMessage = response.getChoices().getFirst().getMessage().payload();

        logger.info("Received response from ChatGPT with payload: {}", chatGPTResponseMessage);

        Message responseMessage = Message.builder()
                .role("assistant")
                .content(chatGPTResponseMessage)
                .chatId(chatId)
                .build();

        messageRepository.save(newMessage);
        logger.debug("Saved new user message to repository with content: {}", newMessage.getContent());

        messageRepository.save(responseMessage);
        logger.debug("Saved assistant response message to repository with content: {}", responseMessage.getContent());

        return response.getChoices().getFirst();
    }

    public ChatGPTResponseChoice<String> sendMessage(MessageRequest request) {
        String defaultChatId = "default";
        logger.info("No chatId provided, using default chatId: {}", defaultChatId);
        return sendMessage(request, defaultChatId);
    }

    public ChatGPTAudioResponse sendAudio(ChatGPTAudioRequest request) {
        logger.info("Sending audio request with model: {}, language: {}", request.model(), request.language());
        return chatGPTClient.sendAudio(request.file(), request.model(), request.language());
    }

    public ChatGPTIntentionResponse findIntention(ChatGPTMessage<String> message) {
        String intentChatId = "intent";
        logger.info("Finding intention with chatId: {}", intentChatId);

        List<ChatGPTMessage<String>> messages = new java.util.ArrayList<>(messageRepository.findByChatId(intentChatId).stream()
                .map(m -> {
                    logger.debug("Fetched intention message from repository with role: {}, content: {}", m.getRole(), m.getContent());
                    return new ChatGPTMessage<>(m.getRole(), m.getContent());
                })
                .toList());

        messages.add(message);
        logger.info("Added new intention message to messages list with content: {}", message.getPayload());

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, Object> responseFormat;

        try {
            responseFormat = objectMapper.readValue(
                    getClass().getClassLoader().getResourceAsStream("responseFormat.json"),
                    new TypeReference<>() {}
            );
            logger.debug("Loaded response format JSON for intention analysis.");
        } catch (Exception e) {
            logger.error("Failed to load response format JSON for intention analysis", e);
            return new ChatGPTIntentionResponse("error", List.of());
        }

        ChatGPTIntentionRequest chatGPTRequest = new ChatGPTIntentionRequest("gpt-4o", messages, responseFormat);
        logger.debug("Sending intention request to ChatGPTClient with model: {}, message count: {}", chatGPTRequest.getModel(), messages.size());

        ChatGPTResponse<ChatGPTIntentionResponse> response = chatGPTClient.sendIntentionMessage(chatGPTRequest);
        ChatGPTIntentionResponse payload = response.getChoices().getFirst().getMessage().payload();

        logger.info("Received intention response with route: {}", payload);
        return payload;
    }
}
