package online.dhbw_studentprojekt.mswetter.client;

import online.dhbw_studentprojekt.dto.wetter.Wetter;
import org.springframework.web.client.RestClient;
import org.springframework.web.service.annotation.PostExchange;

public interface WetterClient extends RestClient {

        @PostExchange("/data/2.5/weather?q=Stuttgart&units=metric")
        Wetter getWetter();
}
