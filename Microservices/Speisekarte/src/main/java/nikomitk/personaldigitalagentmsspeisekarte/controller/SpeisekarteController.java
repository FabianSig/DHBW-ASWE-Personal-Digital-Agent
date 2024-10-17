package nikomitk.personaldigitalagentmsspeisekarte.controller;

import lombok.RequiredArgsConstructor;
import nikomitk.personaldigitalagentmsspeisekarte.service.SpeisekarteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/speisekarte")
public class SpeisekarteController {

    private final SpeisekarteService speisekarteService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public List<String> getSpeisekarte() {
        return speisekarteService.getSpeisekarte();
    }

}
