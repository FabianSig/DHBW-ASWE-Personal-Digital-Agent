package fabiansig.client;

import online.dhbw_studentprojekt.dto.chatgpt.intention.ChatGPTIntentionResponse;
import online.dhbw_studentprojekt.dto.chatgpt.morning.MorningRequest;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatGPTResponseChoice;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatMessageRequest;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.PostExchange;

public interface ChatGPTClient {
    @PostExchange("api/chatgpt/intention")
    ChatGPTIntentionResponse getIntention(@RequestBody String message);

    @PostExchange("api/chatgpt/message/{chatId}")
    ChatGPTResponseChoice getResponse(@RequestBody ChatMessageRequest message, @PathVariable String chatId, @RequestParam String extraPromptId);

    @PostExchange("/api/chatgpt/morning")
    ChatGPTResponseChoice getMorningRoutine(@RequestBody MorningRequest request);
}
