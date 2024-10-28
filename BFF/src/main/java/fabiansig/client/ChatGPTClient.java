package fabiansig.client;

import fabiansig.dto.IntentionResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface ChatGPTClient {
    @PostExchange("api/chatgpt/intention")
    IntentionResponse getIntention(@RequestBody String message);
}
