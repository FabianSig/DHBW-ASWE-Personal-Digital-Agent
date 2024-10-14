package nikomitk.mschatgpt.service;

import lombok.RequiredArgsConstructor;
import nikomitk.mschatgpt.ChatGPTClient;
import nikomitk.mschatgpt.dto.ChatGPTMessage;
import nikomitk.mschatgpt.dto.ChatGPTRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatGPTService {

    @Value("${chatgpt.api.url}")
    private static String API_URL;
    private static final String API_KEY = System.getenv("API_KEY");

    private final ChatGPTClient chatGPTClient;

    public String test(String message) {
        List<ChatGPTMessage> messages = List.of(
                new ChatGPTMessage("system", "You are a helpful assistant."),
                new ChatGPTMessage("user", message)
        );
        ChatGPTRequest request = new ChatGPTRequest("gpt-3.5-turbo", messages);
        return chatGPTClient.test(request);
    }
}
