package com.example.dhbwaswepersonaldigitalagentmswetter.dto;

import java.util.List;

public record Wetter(
        String name,
        Main main
) {
    public record Main(double temp, double feels_like, double temp_min, double temp_max) {
    }

}

