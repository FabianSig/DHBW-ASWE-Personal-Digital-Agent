package fabiansig.client;

import online.dhbw_studentprojekt.dto.chatgpt.intention.ChatGPTIntentionResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface ChatGPTClient {
    @PostExchange("api/chatgpt/intention")
    ChatGPTIntentionResponse getIntention(@RequestBody String message);
}
