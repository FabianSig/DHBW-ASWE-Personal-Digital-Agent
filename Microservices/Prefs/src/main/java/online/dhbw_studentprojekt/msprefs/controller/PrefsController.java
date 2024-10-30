package online.dhbw_studentprojekt.msprefs.controller;

import lombok.RequiredArgsConstructor;
import online.dhbw_studentprojekt.dto.prefs.Preference;
import online.dhbw_studentprojekt.msprefs.service.PrefsService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/prefs")
@RequiredArgsConstructor
public class PrefsController {

    private final PrefsService prefsService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Preference getPref(@RequestParam String id) {
        return prefsService.getPref(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createPref(@RequestBody Preference preference){
        prefsService.createPref(preference);
    }
}
