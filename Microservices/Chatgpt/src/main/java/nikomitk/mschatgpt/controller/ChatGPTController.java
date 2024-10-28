package nikomitk.mschatgpt.controller;

import lombok.RequiredArgsConstructor;
import nikomitk.mschatgpt.dto.*;
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
    public ChatGPTResponseChoice sendMessage(@RequestBody Request request) {
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
    public String findIntention(@RequestBody String message) {
        ChatGPTMessage chatGPTMessage = new ChatGPTMessage("user", message);
        return chatGPTService.findIntention(chatGPTMessage);
    }
}
