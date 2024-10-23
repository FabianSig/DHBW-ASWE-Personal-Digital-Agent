package com.example.dhbwaswepersonaldigitalagentmswetter.client;

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestClient;
import org.springframework.web.service.annotation.PostExchange;

public interface WetterClient extends RestClient {

        @PostExchange("https://api.openweathermap.org/data/2.5/forecast/daily?lat=48.78232&lon=9.17702&cnt=3&appid=DEIN_API_KEY&units=metric&lang=de\n")
        String getWetter(@RequestBody MultiValueMap<String, String> formData);
}
