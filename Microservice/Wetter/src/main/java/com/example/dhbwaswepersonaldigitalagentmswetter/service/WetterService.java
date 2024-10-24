package com.example.dhbwaswepersonaldigitalagentmswetter.service;

import com.example.dhbwaswepersonaldigitalagentmswetter.dto.Wetter;
import com.example.dhbwaswepersonaldigitalagentmswetter.client.WetterClient;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Optional;

@Service
@Slf4j
public class WetterService {

    private final WetterClient wetterClient;

    public WetterService(WetterClient wetterClient) {
        this.wetterClient = wetterClient;
    }

    public Wetter getWetter(Optional<String> datumParam) {
        String datum = datumParam.orElse(LocalDate.now().toString());

        String startThisWeek = LocalDate.now().getDayOfWeek().getValue() <= 3 ? LocalDate.now().minusDays(LocalDate.now().getDayOfWeek().getValue()).toString() : LocalDate.now().plusDays(7 - LocalDate.now().getDayOfWeek().getValue()).toString();
        String startNextWeek = LocalDate.now().getDayOfWeek().getValue() <= 3 ? LocalDate.now().plusDays(7L - LocalDate.now().getDayOfWeek().getValue()).toString() : LocalDate.now().plusDays(14 - LocalDate.now().getDayOfWeek().getValue()).toString();

        String websiteHtml = wetterClient.getWetter();
        return null;
    }

}
