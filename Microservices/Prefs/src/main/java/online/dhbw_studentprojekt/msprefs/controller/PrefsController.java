package online.dhbw_studentprojekt.msprefs.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import online.dhbw_studentprojekt.dto.prefs.Preference;
import online.dhbw_studentprojekt.msprefs.service.PrefsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/prefs")
@RequiredArgsConstructor
public class PrefsController {

    private final PrefsService prefsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get a single preference")
    public Optional<online.dhbw_studentprojekt.msprefs.model.Preference> getPref(@RequestParam String id) {

        return prefsService.getPref(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Create a new preference")
    public void createPref(@RequestBody Preference preference) {

        prefsService.createPref(preference);
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Delete a preference")
    public void deletePref(@RequestParam String id) {

        prefsService.deletePref(id);
    }

}
