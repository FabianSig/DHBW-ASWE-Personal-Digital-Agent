package nikomitk.mschatgpt.client;

import online.dhbw_studentprojekt.dto.chatgpt.audio.ChatGPTAudioResponse;
import online.dhbw_studentprojekt.dto.chatgpt.intention.ChatGPTIntentionRequest;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatGPTRequest;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatGPTResponse;
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
