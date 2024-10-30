package nikomitk.mschatgpt.controller;

import lombok.RequiredArgsConstructor;
import nikomitk.mschatgpt.service.ChatGPTService;
import online.dhbw_studentprojekt.dto.chatgpt.audio.ChatGPTAudioResponse;
import online.dhbw_studentprojekt.dto.chatgpt.intention.ChatGPTIntentionResponse;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatGPTMessage;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatGPTResponseChoice;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatMessageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import nikomitk.mschatgpt.dto.audio.ChatGPTAudioRequest;

@RestController
@RequestMapping("/api/chatgpt")
@RequiredArgsConstructor
public class ChatGPTController {

    private final ChatGPTService chatGPTService;

    @PostMapping("/message/{chatId}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ChatGPTResponseChoice sendMessage(@RequestBody ChatMessageRequest request, @PathVariable String chatId) {

        return chatGPTService.sendMessage(request, chatId);
    }

    @PostMapping("/message")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ChatGPTResponseChoice sendMessage(@RequestBody ChatMessageRequest request) {
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
        ChatGPTMessage chatGPTMessage = new ChatGPTMessage("user", message);
        return chatGPTService.findIntention(chatGPTMessage);
    }
}
