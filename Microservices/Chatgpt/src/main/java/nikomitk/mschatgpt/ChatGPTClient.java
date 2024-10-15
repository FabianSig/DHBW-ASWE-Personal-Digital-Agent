package nikomitk.mschatgpt;

import nikomitk.mschatgpt.dto.ChatGPTRequest;
import nikomitk.mschatgpt.dto.ChatGPTResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface ChatGPTClient {

    @PostExchange("v1/chat/completions")
    ChatGPTResponse sendMessage(@RequestBody ChatGPTRequest request);
}
