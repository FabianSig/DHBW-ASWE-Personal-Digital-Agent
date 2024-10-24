package com.example.dhbwaswepersonaldigitalagentmswetter.client;

import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestClient;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface WetterClient extends RestClient {

        @PostExchange("/data/2.5/weather?q=Stuttgart&appid=system.getenv('API_KEY')")
        String getWetter();
}
