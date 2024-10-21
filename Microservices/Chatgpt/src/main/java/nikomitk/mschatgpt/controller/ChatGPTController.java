package nikomitk.mschatgpt.controller;

import lombok.RequiredArgsConstructor;
import nikomitk.mschatgpt.dto.ChatGPTAudioRequest;
import nikomitk.mschatgpt.dto.ChatGPTAudioResponse;
import nikomitk.mschatgpt.dto.ChatGPTResponseChoice;
import nikomitk.mschatgpt.dto.Request;
import nikomitk.mschatgpt.service.ChatGPTService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/chatgpt")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ChatGPTController {

    private static final Logger logger = LoggerFactory.getLogger(ChatGPTController.class);
    private final ChatGPTService chatGPTService;

    @PostMapping("/message")
    @ResponseStatus(HttpStatus.OK)
    public ChatGPTResponseChoice sendMessage(@RequestBody Request request) {
        logger.info("Received message request: {}", request);
        try {
            ChatGPTResponseChoice response = chatGPTService.sendMessage(request);
            logger.info("Message response: {}", response);
            return response;
        } catch (Exception e) {
            logger.error("Error while processing message request: {}", request, e);
            throw e;
        }
    }

    @PostMapping("/audio")
    @ResponseStatus(HttpStatus.OK)
    public ChatGPTAudioResponse sendAudio(@RequestPart("file") MultipartFile file) {
        logger.info("Received audio file: {}", file.getOriginalFilename());
        ChatGPTAudioRequest audioRequest = new ChatGPTAudioRequest(file, "whisper-1", "de");
        try {
            ChatGPTAudioResponse response = chatGPTService.sendAudio(audioRequest);
            logger.info("Audio response: {}", response);
            return response;
        } catch (Exception e) {
            logger.error("Error while processing audio request for file: {}", file.getOriginalFilename(), e);
            throw e;
        }
    }
}
