package com.example.dhbwaswepersonaldigitalagentmswetter.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/api/wetter")

public class WetterController {

    private final wetterService wetterService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Wetter getWetter(@io.swagger.v3.oas.annotations.Parameter(example = "dd-MM-yyyy") @RequestParam Optional<String> datum) {
        return wetterService.getWetter(datum);
    }

}
