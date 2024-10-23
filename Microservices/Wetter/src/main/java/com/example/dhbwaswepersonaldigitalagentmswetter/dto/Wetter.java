package com.example.dhbwaswepersonaldigitalagentmswetter.dto;

import java.util.List;

public record Wetter(
        List<Double> temperatur,
        List<String> wetterBeschreibung,
        List<Double> windgeschwindigkeit,
        List<Integer> luftfeuchtigkeit,
        List<Double> niederschlagswahrscheinlichkeit,
        List<String> datum
) {}
