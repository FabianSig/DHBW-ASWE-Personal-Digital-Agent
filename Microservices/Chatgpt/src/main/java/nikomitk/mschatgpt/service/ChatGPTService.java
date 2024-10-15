package nikomitk.mschatgpt.service;

import lombok.RequiredArgsConstructor;
import nikomitk.mschatgpt.ChatGPTClient;
import nikomitk.mschatgpt.dto.ChatGPTMessage;
import nikomitk.mschatgpt.dto.ChatGPTRequest;
import nikomitk.mschatgpt.dto.ChatGPTResponse;
import nikomitk.mschatgpt.model.Message;
import nikomitk.mschatgpt.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatGPTService {


    private final MessageRepository messageRepository;
    private final ChatGPTClient chatGPTClient;

    public String sendMessage(String message) {

        List<ChatGPTMessage> messages = new java.util.ArrayList<>(messageRepository.findAll().stream().map(m -> new ChatGPTMessage(m.getRole(), m.getContent())).toList());

        Message newMessage = Message.builder().role("user").content(message).build();
        messages.add(new ChatGPTMessage(newMessage.getRole(), newMessage.getContent()));

        ChatGPTRequest request = new ChatGPTRequest("gpt-4o-mini", messages);
        ChatGPTResponse response = chatGPTClient.test(request);
        String chatGPTResponseContent = response.choices().getFirst().message().content();

        Message responseMessage = Message.builder().role("assistant").content(chatGPTResponseContent).build();
        messageRepository.save(newMessage);
        messageRepository.save(responseMessage);

        return chatGPTResponseContent;
    }
}
