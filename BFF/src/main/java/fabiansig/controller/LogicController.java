package fabiansig.controller;

import fabiansig.service.RoutineService;
import fabiansig.service.TriggerService;
import online.dhbw_studentprojekt.dto.chatgpt.standard.MessageRequest;
import fabiansig.service.IntentionMessageService;
import lombok.RequiredArgsConstructor;
import online.dhbw_studentprojekt.dto.trigger.TriggerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logic")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LogicController {

    private final IntentionMessageService logicService;
    private final TriggerService triggerService;
    private final RoutineService routineService;

    @PostMapping("/message")
    @ResponseStatus(HttpStatus.OK)
    public String sendMessage(@RequestBody MessageRequest request) {
        return logicService.sendResponseMessage(request);
    }

    @GetMapping("/trigger")
    @ResponseStatus(HttpStatus.OK)
    public TriggerResponse getTrigger(@RequestParam String date) {
        return triggerService.getTrigger(date);
    }

    @GetMapping("/morning")
    @ResponseStatus(HttpStatus.OK)
    public String getMorningRoutine(){
        return routineService.getMorningRoutine();
    }

    @GetMapping("/mittag")
    @ResponseStatus(HttpStatus.OK)
    public String getMittagRoutine(){
        return routineService.getMittagRoutine();
    }
}
