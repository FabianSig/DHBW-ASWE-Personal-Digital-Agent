package fabiansig.controller;

import fabiansig.service.TriggerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/trigger")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class TriggerController {

    private final TriggerService logicService;

    @PostMapping("/{date}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<String> getTrigger(@RequestParam String date) {
        return logicService.getTrigger(date);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.ACCEPTED)
    public List<String> getTrigger() {
        return logicService.getTrigger();
    }
}
