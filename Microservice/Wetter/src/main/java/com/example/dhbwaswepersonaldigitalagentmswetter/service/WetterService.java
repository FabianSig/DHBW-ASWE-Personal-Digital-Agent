package com.example.dhbwaswepersonaldigitalagentmswetter.service;

import com.example.dhbwaswepersonaldigitalagentmswetter.dto.Wetter;
import com.example.dhbwaswepersonaldigitalagentmswetter.client.WetterClient;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class WetterService {

    private final WetterClient wetterClient;

    public WetterService(WetterClient wetterClient) {
        this.wetterClient = wetterClient;
    }

    private static List<String> extractMenu(String gruppe) {
        String[] split = gruppe.split("<span style='font-size:1.5em'>");
        return Arrays.stream(split)
                .map(s -> s.split("</span>")[0])
                .dropWhile(s -> s.contains("<div"))
                .toList();
    }

    public Wetter getWetter(Optional<String> datumParam) {
        String datum = datumParam.orElse(LocalDate.now().toString());

        String startThisWeek = LocalDate.now().getDayOfWeek().getValue() <= 3 ? LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue()).toString() : LocalDate.now().plusDays(7 - LocalDate.now().getDayOfWeek().getValue()).toString();
        String startNextWeek = LocalDate.now().getDayOfWeek().getValue() <= 3 ? LocalDate.now().plusDays(7L - LocalDate.now().getDayOfWeek().getValue()).toString() : LocalDate.now().plusDays(14 - LocalDate.now().getDayOfWeek().getValue()).toString();


        return wetterClient.getWetter();

    }

}
