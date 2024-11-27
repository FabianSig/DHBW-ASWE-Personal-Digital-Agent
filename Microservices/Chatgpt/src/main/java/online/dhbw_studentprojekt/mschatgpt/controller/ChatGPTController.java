package online.dhbw_studentprojekt.mschatgpt.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.dhbw_studentprojekt.dto.chatgpt.audio.TTSRequest;
import online.dhbw_studentprojekt.mschatgpt.service.ChatGPTService;
import online.dhbw_studentprojekt.dto.chatgpt.audio.ChatGPTAudioResponse;
import online.dhbw_studentprojekt.dto.chatgpt.intention.ChatGPTIntentionResponse;
import online.dhbw_studentprojekt.dto.chatgpt.morning.MorningRequest;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatGPTMessage;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatGPTResponseChoice;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatMessageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import online.dhbw_studentprojekt.mschatgpt.dto.audio.ChatGPTAudioRequest;

@Slf4j
@RestController
@RequestMapping("/api/chatgpt")
@RequiredArgsConstructor
public class ChatGPTController {

    private final ChatGPTService chatGPTService;

    @PostMapping("/message/{chatId}")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Send a message to an existing chat.")
    public ChatGPTResponseChoice sendMessage(@RequestBody ChatMessageRequest request, @PathVariable String chatId, @RequestParam(required = false) String extraPromptId) {
        return chatGPTService.sendMessage(request, chatId, extraPromptId);
    }

    @PostMapping("/message")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Send a message to the chatbot without context.")
    public ChatGPTResponseChoice sendMessage(@RequestBody ChatMessageRequest request, @RequestParam(required = false) String extraPromptId) {
        return chatGPTService.sendMessage(request, extraPromptId);
    }

    @PostMapping("/audio")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Send an audio file to transcribe it.")
    public ChatGPTAudioResponse sendAudio(@RequestPart("file") MultipartFile file) {
        ChatGPTAudioRequest audioRequest = new ChatGPTAudioRequest(file, "whisper-1", "de");
        return chatGPTService.sendAudio(audioRequest);
    }

    @PostMapping("/intention")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Find the intention of a message and map it to a microservice.")
    public ChatGPTIntentionResponse findIntention(@RequestBody String message) {
        ChatGPTMessage chatGPTMessage = new ChatGPTMessage("user", message);
        return chatGPTService.findIntention(chatGPTMessage);
    }
    
    @PostMapping("/morning")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a morning message.")
    public ChatGPTResponseChoice getMorning(@RequestBody  MorningRequest request) {
        return chatGPTService.getMorning(request);
    }

    @PostMapping(value = "/tts", produces = "audio/mpeg")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get TTS.")
    public ResponseEntity<byte[]> getTTS(@RequestBody TTSRequest request) {
        byte[] audioData = chatGPTService.getTTS(request.text());

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_TYPE, "audio/mpeg");
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"speech.mp3\"");

        return new ResponseEntity<>(audioData, headers, HttpStatus.OK);
    }
    
}
