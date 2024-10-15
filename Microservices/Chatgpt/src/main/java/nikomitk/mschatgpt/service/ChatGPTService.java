package nikomitk.mschatgpt.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nikomitk.mschatgpt.ChatGPTClient;
import nikomitk.mschatgpt.dto.ChatGPTMessage;
import nikomitk.mschatgpt.dto.ChatGPTRequest;
import nikomitk.mschatgpt.model.Message;
import nikomitk.mschatgpt.repository.MessageRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatGPTService {


    private final MessageRepository messageRepository;
    private final ChatGPTClient chatGPTClient;

    public String test(String message) {

        log.info("Start Service");
        long time = System.currentTimeMillis();
        List<ChatGPTMessage> messages = new java.util.ArrayList<>(messageRepository.findAll().stream().map(m -> new ChatGPTMessage(m.getRole(), m.getContent())).toList());
        long time2 = System.currentTimeMillis();
        log.info("Time after DB read: " + (time2 - time));
        long time3 = System.currentTimeMillis();
        Message newMessage = Message.builder().role("user").content(message).build();
        long time4 = System.currentTimeMillis();
        log.info("Time after new message: " + (time4 - time3));
        long time5 = System.currentTimeMillis();
        messages.add(new ChatGPTMessage(newMessage.getRole(), newMessage.getContent()));
        long time6 = System.currentTimeMillis();
        log.info("Time after add new message: " + (time6 - time5));

        ChatGPTRequest request = new ChatGPTRequest("gpt-4o-mini", messages);
        long time7 = System.currentTimeMillis();
        String response = chatGPTClient.test(request);
        long time8 = System.currentTimeMillis();
        log.info("Time after GPT call: " + (time8 - time7));

        Message responseMessage = Message.builder().role("assistant").content(response).build();
        long time9 = System.currentTimeMillis();
        messageRepository.save(newMessage);
        long time10 = System.currentTimeMillis();
        log.info("Time after save new message: " + (time10 - time9));
        long time11 = System.currentTimeMillis();
        messageRepository.save(responseMessage);
        long time12 = System.currentTimeMillis();
        log.info("Time after save response message: " + (time12 - time11));

        return response;
    }
}
