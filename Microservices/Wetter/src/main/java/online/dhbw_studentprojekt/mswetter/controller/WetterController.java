package online.dhbw_studentprojekt.mswetter.controller;

import lombok.RequiredArgsConstructor;
import online.dhbw_studentprojekt.dto.wetter.Wetter;
import online.dhbw_studentprojekt.mswetter.service.WetterService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wetter")
public class WetterController {

    private final WetterService wetterService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Wetter getWetter() {

        return wetterService.getWetter();
    }

}
