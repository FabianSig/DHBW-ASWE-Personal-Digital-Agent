package online.dhbw_studentprojekt.bff.controller;

import lombok.RequiredArgsConstructor;
import online.dhbw_studentprojekt.bff.service.IntentionMessageService;
import online.dhbw_studentprojekt.bff.service.RoutineService;
import online.dhbw_studentprojekt.bff.service.TriggerService;
import online.dhbw_studentprojekt.dto.chatgpt.standard.MessageRequest;
import online.dhbw_studentprojekt.dto.trigger.TriggerResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/logic")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class LogicController {

    private final IntentionMessageService intentionMessageService;
    private final TriggerService triggerService;
    private final RoutineService routineService;

    @PostMapping("/message")
    @ResponseStatus(HttpStatus.OK)
    public String sendMessage(@RequestBody MessageRequest request) {

        return intentionMessageService.getResponseMessage(request);
    }

    @GetMapping("/trigger")
    @ResponseStatus(HttpStatus.OK)
    public TriggerResponse getTrigger(@RequestParam String date) {

        return triggerService.getTrigger(date);
    }

    @GetMapping("/morning")
    @ResponseStatus(HttpStatus.OK)
    public String getMorningRoutine() {

        return routineService.getMorningRoutine();
    }

    @GetMapping("/mittag")
    @ResponseStatus(HttpStatus.OK)
    public String getMittagRoutine() {

        return routineService.getMittagRoutine();
    }


    @GetMapping("/nachmittag")
    @ResponseStatus(HttpStatus.OK)
    public String getNachMittagRoutine() {

        return routineService.getNachmittagRoutine();
    }

    @GetMapping("/abend")
    @ResponseStatus(HttpStatus.OK)
    public String getAbendRoutine() {

        return routineService.getAbendRoutine();
    }

}
