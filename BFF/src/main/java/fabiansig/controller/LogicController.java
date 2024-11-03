package fabiansig.controller;

import fabiansig.service.TriggerService;
import online.dhbw_studentprojekt.dto.chatgpt.standard.MessageRequest;
import fabiansig.service.LogicService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/logic")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LogicController {

    private final LogicService logicService;
    private final TriggerService triggerService;

    @PostMapping("/message")
    @ResponseStatus(HttpStatus.OK)
    public String sendMessage(@RequestBody MessageRequest request) {
        return logicService.sendResponseMessage(request);
    }

    @PostMapping("/trigger/{date}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<String> getTrigger(@RequestParam String date) {
        return triggerService.getTrigger(date);
    }

    @PostMapping("/trigger")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<String> getTrigger() {
        return triggerService.getTrigger();
    }

    @GetMapping("/morning")
    @ResponseStatus(HttpStatus.OK)
    public String getMorningRoutine(){
        return logicService.getMorningRoutine();
    }
}
