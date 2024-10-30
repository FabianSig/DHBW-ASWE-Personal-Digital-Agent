package fabiansig.client;

import online.dhbw_studentprojekt.dto.chatgpt.intention.ChatGPTIntentionResponse;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatGPTResponse;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatMessageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface ChatGPTClient {
    @PostExchange("api/chatgpt/intention")
    ChatGPTIntentionResponse getIntention(@RequestBody String message);

    @PostExchange("api/chatgpt/message/{chatId}")
    ChatGPTResponse getResponse(@RequestBody ChatMessageRequest message, @PathVariable String chatId);
}
