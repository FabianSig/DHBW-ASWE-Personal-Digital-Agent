package online.dhbw_studentprojekt.mschatgpt.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.dhbw_studentprojekt.dto.chatgpt.audio.ChatGPTAudioResponse;
import online.dhbw_studentprojekt.dto.chatgpt.intention.ChatGPTIntentionRequest;
import online.dhbw_studentprojekt.dto.chatgpt.intention.ChatGPTIntentionResponse;
import online.dhbw_studentprojekt.dto.chatgpt.morning.MorningRequest;
import online.dhbw_studentprojekt.dto.chatgpt.standard.*;
import online.dhbw_studentprojekt.dto.stock.Stock;
import online.dhbw_studentprojekt.mschatgpt.client.ChatGPTClient;
import online.dhbw_studentprojekt.mschatgpt.dto.audio.ChatGPTAudioRequest;
import online.dhbw_studentprojekt.mschatgpt.model.Message;
import online.dhbw_studentprojekt.mschatgpt.model.Prompt;
import online.dhbw_studentprojekt.mschatgpt.repository.MessageRepository;
import online.dhbw_studentprojekt.mschatgpt.repository.PromptRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatGPTService {


    private static final String GPT_MODEL = "gpt-4o-mini";
    private final MessageRepository messageRepository;
    private final PromptRepository promptRepository;
    private final ChatGPTClient chatGPTClient;

    /**
     * Sends a message to ChatGPT and returns the response.
     *
     * @param request       the chat message request
     * @param extraPromptId an optional extra prompt ID
     * @return the response choice from ChatGPT
     */
    public ChatGPTResponseChoice sendMessage(ChatMessageRequest request, String extraPromptId) {

        String defaultChatId = "default";
        return sendMessage(request, defaultChatId, extraPromptId);
    }

    /**
     * Sends a message to ChatGPT and returns the response.
     *
     * @param request       the chat message request
     * @param extraPromptId an optional extra prompt ID
     * @return the response choice from ChatGPT
     */
    public ChatGPTResponseChoice sendMessage(ChatMessageRequest request, String chatId, String extraPromptId) {

        // Retrieve the default prompt for messages
        ChatGPTMessage prompt = promptRepository.findFirstByPromptId("message")
                .map(m -> new ChatGPTMessage(m.getRole(), m.getContent()))
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Prompt not found"));

        // Retrieve the last 3 messages from the chat history
        List<ChatGPTMessage> messages = messageRepository.findByChatId(chatId).stream()
                .map(m -> new ChatGPTMessage(m.getRole(), m.getContent()))
                .collect(Collectors.collectingAndThen(Collectors.toList(), list ->
                        list.size() > 3 ? list.subList(list.size() - 3, list.size()) : list
                ));

        messages.addFirst(prompt);
        messages.add(1, new ChatGPTMessage("system", request.data()));

        // If an extra prompt ID is provided, add the corresponding prompt to the list of messages
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

        // Send the message to ChatGPT
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

    /**
     * Sends an audio file to ChatGPT for transcription and returns the response.
     *
     * @param request the audio request containing the file, model, and language
     * @return the audio response from ChatGPT
     */
    public ChatGPTAudioResponse sendAudio(ChatGPTAudioRequest request) {

        return chatGPTClient.sendAudio(request.file(), request.model(), request.language());
    }

    /**
     * Finds the intention of a given message using ChatGPT and returns the intention response.
     *
     * @param message the chat message
     * @return the intention response from ChatGPT
     */
    public ChatGPTIntentionResponse findIntention(ChatGPTMessage message) {

        // Get the current date and time
        final String currentDate = LocalDate.now().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"));
        final String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        // Retrieve the needed prompts and format them with the current date and time
        final List<ChatGPTMessage> messages = new ArrayList<>(promptRepository.findByPromptId("intent").stream()
                .map(m -> new ChatGPTMessage(m.getRole(), String.format(m.getContent(), currentDate, currentTime)))
                .toList());

        messages.add(message);

        // Read the response format from a JSON file
        final ObjectMapper objectMapper = new ObjectMapper();
        final Map<String, Object> responseFormat;

        try (InputStream is = getClass().getClassLoader().getResourceAsStream("responseFormat.json")) {
            responseFormat = objectMapper.readValue(is, new TypeReference<>() {});
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading response format", e);
        }

        // Send the intention message to ChatGPT
        final ChatGPTIntentionRequest chatGPTRequest = new ChatGPTIntentionRequest(GPT_MODEL, messages, responseFormat);

        try {
            String strResponse = chatGPTClient.sendIntentionMessage(chatGPTRequest).choices().getFirst().message().content();
            return objectMapper.readValue(strResponse, ChatGPTIntentionResponse.class);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error reading response format", e);
        }

    }

    /**
     * Generates a morning message using ChatGPT based on the given request.
     *
     * @param request the morning request containing headlines and stocks
     * @return the response choice from ChatGPT
     */
    public ChatGPTResponseChoice getMorning(MorningRequest request) {

        // Retrieve the morning prompt
        final Prompt prompt = promptRepository.findFirstByPromptId("morning")
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Prompt not found"));

        // Format the prompt with the request data
        final String content = String.format(prompt.getContent(),
                request.unreadMails(),
                request.contactsLastCalled(),
                request.firstHeadline(),
                request.secondHeadline(),
                request.thirdHeadline(),
                request.stocks().stream()
                        .map(Stock::toString)
                        .toList());
        final ChatGPTMessage promptMessage = new ChatGPTMessage(prompt.getRole(), content);

        // Send the morning message to ChatGPT
        final ChatGPTRequest chatGPTRequest = new ChatGPTRequest(GPT_MODEL, List.of(promptMessage));
        ChatGPTResponse response = chatGPTClient.sendMessage(chatGPTRequest);

        return response.choices().getFirst();
    }

    public byte[]  getTTS(String message){

        String summaryMessage = sendMessage(new ChatMessageRequest(message, "No data"), "summary", "summary").message().content();

        Map<String, String> requestBody = Map.of(
                "voice", "alloy",
                "model", "tts-1",
                "input", summaryMessage
        );

        log.info(summaryMessage);

        return chatGPTClient.getTTS(requestBody);
    }

}
