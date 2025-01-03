package online.dhbw_studentprojekt.msspeisekarte.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.dhbw_studentprojekt.dto.speisekarte.Speisekarte;
import online.dhbw_studentprojekt.msspeisekarte.service.SpeisekarteService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/speisekarte")
@Slf4j
public class SpeisekarteController {

    private final SpeisekarteService speisekarteService;

    /**
     * Get the speisekarte for the given date.
     *
     * @param datum in format yyyy-MM-dd
     * @return the speisekarte for the given date
     * @see Speisekarte
     */
    @Operation(summary = "Get the speisekarte for the given date")
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Speisekarte getSpeisekarte(@io.swagger.v3.oas.annotations.Parameter(example = "yyyy-MM-dd") @RequestParam Optional<String> datum) {

        return speisekarteService.getSpeisekarte(datum);
    }

    /**
     * Get the speisekarte filtered with allergenes for the given date.
     *
     * @param datum in format yyyy-MM-dd
     * @return the speisekarte for the given date
     * @see Speisekarte
     */
    @Operation(summary = "Get the speisekarte for the given date, with filtered allergene")
    @GetMapping("/allergene")
    @ResponseStatus(HttpStatus.OK)
    public Speisekarte getSpeisekarteWithFilteredAllergene(@io.swagger.v3.oas.annotations.Parameter(example = "yyyy-MM-dd") @RequestParam Optional<String> datum, @RequestParam(required = false) List<String> allergene) {

        return speisekarteService.getSpeisekarteWithFilteredAllergene(datum, allergene);
    }

}
