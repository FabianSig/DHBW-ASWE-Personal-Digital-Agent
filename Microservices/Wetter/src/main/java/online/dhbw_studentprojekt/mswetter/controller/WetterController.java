package online.dhbw_studentprojekt.mswetter.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import online.dhbw_studentprojekt.dto.wetter.Wetter;
import online.dhbw_studentprojekt.mswetter.service.WetterService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wetter")
public class WetterController {

    private final WetterService wetterService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get the current weather")
    public Wetter getWetter(@RequestParam String city) {
        return wetterService.getWetter(city);
    }

}
