package nikomitk.personaldigitalagentmsspeisekarte.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nikomitk.personaldigitalagentmsspeisekarte.Speisekarte;
import nikomitk.personaldigitalagentmsspeisekarte.service.SpeisekarteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/speisekarte")
@Slf4j
public class SpeisekarteController {

    private final SpeisekarteService speisekarteService;

    /**
     * Test
     *
     * @param datum in format yyyy-MM-dd
     */
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Speisekarte getSpeisekarte(@RequestParam Optional<String> datum) {
        return speisekarteService.getSpeisekarte(datum);
    }

}
