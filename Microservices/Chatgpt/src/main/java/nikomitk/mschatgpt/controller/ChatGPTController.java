package nikomitk.mschatgpt.controller;

import lombok.RequiredArgsConstructor;
import nikomitk.mschatgpt.dto.audio.ChatGPTAudioRequest;
import nikomitk.mschatgpt.dto.audio.ChatGPTAudioResponse;
import nikomitk.mschatgpt.dto.intention.ChatGPTIntentionRequest;
import nikomitk.mschatgpt.dto.intention.ChatGPTIntentionResponse;
import nikomitk.mschatgpt.dto.standard.ChatGPTMessage;
import nikomitk.mschatgpt.dto.standard.ChatGPTResponseChoice;
import nikomitk.mschatgpt.dto.standard.MessageRequest;
import nikomitk.mschatgpt.service.ChatGPTService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/chatgpt")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ChatGPTController {

    private final ChatGPTService chatGPTService;

    @PostMapping("/message")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ChatGPTResponseChoice<String> sendMessage(@RequestBody MessageRequest request) {
        return chatGPTService.sendMessage(request);
    }

    @PostMapping("/audio")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ChatGPTAudioResponse sendAudio(@RequestPart("file") MultipartFile file) {
        ChatGPTAudioRequest audioRequest = new ChatGPTAudioRequest(file, "whisper-1", "de");
        return chatGPTService.sendAudio(audioRequest);
    }

    @PostMapping("/intention")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ChatGPTIntentionResponse findIntention(@RequestBody String message) {
        ChatGPTMessage<String> chatGPTMessage = new ChatGPTMessage<>("user", message);
        return chatGPTService.findIntention(chatGPTMessage);
    }
}
