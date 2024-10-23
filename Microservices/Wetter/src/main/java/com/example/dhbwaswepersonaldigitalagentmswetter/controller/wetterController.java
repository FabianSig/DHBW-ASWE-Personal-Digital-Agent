package com.example.dhbwaswepersonaldigitalagentmswetter.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
@RequestMapping("/api/wetter")
@Slf4j


public class WetterController {

    private final wetterService wetterService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Wetter getWetter(@io.swagger.v3.oas.annotations.Parameter(example = "dd-MM-yyyy") @RequestParam Optional<String> datum) {
        return wetterService.getWetter(datum);
    }

}
