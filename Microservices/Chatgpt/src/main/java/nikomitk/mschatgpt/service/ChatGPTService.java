package nikomitk.mschatgpt.service;

import lombok.RequiredArgsConstructor;
import nikomitk.mschatgpt.client.ChatGPTClient;
import nikomitk.mschatgpt.dto.ChatGPTAudioResponse;
import nikomitk.mschatgpt.dto.*;
import nikomitk.mschatgpt.model.Message;
import nikomitk.mschatgpt.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatGPTService {


    private final MessageRepository messageRepository;
    private final ChatGPTClient chatGPTClient;

    public ChatGPTResponseChoice sendMessage(Request request, String chatId) {

        List<ChatGPTMessage> messages = new java.util.ArrayList<>(messageRepository.findByChatId(chatId).stream().map(m -> new ChatGPTMessage(m.getRole(), m.getContent())).toList());

        Message newMessage = Message.builder().role("user").content(request.message()).chatId(chatId).build();
        messages.add(new ChatGPTMessage(newMessage.getRole(), newMessage.getContent()));

        ChatGPTRequest chatGPTRequest = new ChatGPTRequest("gpt-4o", messages);
        ChatGPTResponse response = chatGPTClient.sendMessage(chatGPTRequest);
        String chatGPTResponseMessage = response.choices().getFirst().message().content();

        Message responseMessage = Message.builder().role("assistant").content(chatGPTResponseMessage).build();
        messageRepository.save(newMessage);
        messageRepository.save(responseMessage);

        return response.choices().getFirst();
    }

    public ChatGPTResponseChoice sendMessage(Request request) {
        String defaultChatId = "default";
        return sendMessage(request, defaultChatId);
    }

    public ChatGPTAudioResponse sendAudio(ChatGPTAudioRequest request) {

        return chatGPTClient.sendAudio(request.file(), request.model(), request.language());
    }

    public ChatGPTResponseChoice findIntention(ChatGPTMessage message) {

        String intentChatId = "intent";
        List<ChatGPTMessage> messages = new java.util.ArrayList<>(messageRepository.findByChatId(intentChatId).stream().map(m -> new ChatGPTMessage(m.getRole(), m.getContent())).toList());

        messages.add(message);

        ChatGPTRequest chatGPTRequest = new ChatGPTRequest("gpt-4o", messages);
        ChatGPTResponse response = chatGPTClient.sendMessage(chatGPTRequest);

        return response.choices().getFirst();


    }


}
