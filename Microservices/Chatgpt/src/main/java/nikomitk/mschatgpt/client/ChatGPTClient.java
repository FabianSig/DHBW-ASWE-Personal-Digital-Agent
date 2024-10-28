package nikomitk.mschatgpt.client;

import nikomitk.mschatgpt.dto.ChatGPTAudioResponse;
import nikomitk.mschatgpt.dto.ChatGPTIntentionRequest;
import nikomitk.mschatgpt.dto.ChatGPTRequest;
import nikomitk.mschatgpt.dto.ChatGPTResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.service.annotation.PostExchange;

public interface ChatGPTClient {

    @PostExchange("v1/chat/completions")
    ChatGPTResponse sendMessage(@RequestBody ChatGPTRequest request);

    @PostExchange("v1/chat/completions")
    ChatGPTResponse sendIntentionMessage(@RequestBody ChatGPTIntentionRequest request);

    @PostExchange(value = "v1/audio/transcriptions", contentType = "multipart/form-data")
    ChatGPTAudioResponse sendAudio(@RequestPart("file") MultipartFile file,
                                   @RequestPart("model") String model,
                                   @RequestPart("language") String language);
}
