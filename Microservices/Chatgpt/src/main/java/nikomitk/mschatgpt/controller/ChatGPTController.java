package nikomitk.mschatgpt.controller;

import lombok.RequiredArgsConstructor;
import nikomitk.mschatgpt.dto.ChatGPTResponseChoice;
import nikomitk.mschatgpt.dto.Request;
import nikomitk.mschatgpt.service.ChatGPTService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/chatgpt")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ChatGPTController {

    private final ChatGPTService chatGPTService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ChatGPTResponseChoice sendMessage(@RequestBody Request request) {
        return chatGPTService.sendMessage(request);
    }
}
