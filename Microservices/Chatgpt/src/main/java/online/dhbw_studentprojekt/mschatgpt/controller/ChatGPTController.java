package online.dhbw_studentprojekt.mschatgpt.controller;

import lombok.RequiredArgsConstructor;
import online.dhbw_studentprojekt.mschatgpt.service.ChatGPTService;
import online.dhbw_studentprojekt.dto.chatgpt.audio.ChatGPTAudioResponse;
import online.dhbw_studentprojekt.dto.chatgpt.intention.ChatGPTIntentionResponse;
import online.dhbw_studentprojekt.dto.chatgpt.morning.MorningRequest;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatGPTMessage;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatGPTResponseChoice;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatMessageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import online.dhbw_studentprojekt.mschatgpt.dto.audio.ChatGPTAudioRequest;

@RestController
@RequestMapping("/api/chatgpt")
@RequiredArgsConstructor
public class ChatGPTController {

    private final ChatGPTService chatGPTService;

    @PostMapping("/message/{chatId}")
    @ResponseStatus(HttpStatus.OK)
    public ChatGPTResponseChoice sendMessage(@RequestBody ChatMessageRequest request, @PathVariable String chatId, @RequestParam(required = false) String extraPromptId) {
        return chatGPTService.sendMessage(request, chatId, extraPromptId);
    }

    @PostMapping("/message")
    @ResponseStatus(HttpStatus.OK)
    public ChatGPTResponseChoice sendMessage(@RequestBody ChatMessageRequest request, @RequestParam(required = false) String extraPromptId) {
        return chatGPTService.sendMessage(request, extraPromptId);
    }

    @PostMapping("/audio")
    @ResponseStatus(HttpStatus.OK)
    public ChatGPTAudioResponse sendAudio(@RequestPart("file") MultipartFile file) {
        ChatGPTAudioRequest audioRequest = new ChatGPTAudioRequest(file, "whisper-1", "de");
        return chatGPTService.sendAudio(audioRequest);
    }

    @PostMapping("/intention")
    @ResponseStatus(HttpStatus.OK)
    public ChatGPTIntentionResponse findIntention(@RequestBody String message) {
        ChatGPTMessage chatGPTMessage = new ChatGPTMessage("user", message);
        return chatGPTService.findIntention(chatGPTMessage);
    }
    
    @PostMapping("/morning")
    @ResponseStatus(HttpStatus.OK)
    public ChatGPTResponseChoice getMorning(@RequestBody  MorningRequest request) {
        return chatGPTService.getMorning(request);
    }
    
}
