package nikomitk.personaldigitalagentmsspeisekarte.controller;

import lombok.RequiredArgsConstructor;
import nikomitk.personaldigitalagentmsspeisekarte.Speisekarte;
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

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Speisekarte getSpeisekarte() {
        return speisekarteService.getSpeisekarte();
    }

}
