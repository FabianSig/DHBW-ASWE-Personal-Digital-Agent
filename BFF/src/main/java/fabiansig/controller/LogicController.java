package fabiansig.controller;

import online.dhbw_studentprojekt.dto.chatgpt.standard.MessageRequest;
import fabiansig.service.LogicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logic")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LogicController {

    private final LogicService logicService;

    @PostMapping("/message")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public String sendMessage(@RequestBody MessageRequest request) {
        return logicService.sendMessage(request);
    }

}
