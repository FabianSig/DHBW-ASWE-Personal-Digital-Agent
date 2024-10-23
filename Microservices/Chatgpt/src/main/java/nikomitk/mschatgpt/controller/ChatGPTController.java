package nikomitk.mschatgpt.controller;

import lombok.RequiredArgsConstructor;
import nikomitk.mschatgpt.dto.ChatGPTAudioRequest;
import nikomitk.mschatgpt.dto.ChatGPTAudioResponse;
import nikomitk.mschatgpt.dto.ChatGPTResponseChoice;
import nikomitk.mschatgpt.dto.Request;
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
    @ResponseStatus(HttpStatus.OK)
    public ChatGPTResponseChoice sendMessage(@RequestBody Request request) {
        return chatGPTService.sendMessage(request);
    }

    @PostMapping("/audio")
    @ResponseStatus(HttpStatus.OK)
    public ChatGPTAudioResponse sendAudio(@RequestPart("file") MultipartFile file) {
        ChatGPTAudioRequest audioRequest = new ChatGPTAudioRequest(file, "whisper-1", "de");
        return chatGPTService.sendAudio(audioRequest);
    }
}
