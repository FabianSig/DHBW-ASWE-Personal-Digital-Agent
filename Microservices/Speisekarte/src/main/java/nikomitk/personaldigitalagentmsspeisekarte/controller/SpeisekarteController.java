package nikomitk.personaldigitalagentmsspeisekarte.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import nikomitk.personaldigitalagentmsspeisekarte.dto.Speisekarte;
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
     * Get the speisekarte for the given date.
     *
     * @param datum in format yyyy-MM-dd
     * @return the speisekarte for the given date
     * @see nikomitk.personaldigitalagentmsspeisekarte.dto.Speisekarte
     */
    @Operation(summary = "Get the speisekarte for the given date")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Speisekarte getSpeisekarte(@io.swagger.v3.oas.annotations.Parameter(example = "dd-MM-yyyy") @RequestParam Optional<String> datum) {
        return speisekarteService.getSpeisekarte(datum);
    }

}
