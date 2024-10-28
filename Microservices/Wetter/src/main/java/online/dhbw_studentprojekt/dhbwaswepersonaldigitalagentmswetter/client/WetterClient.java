package online.dhbw_studentprojekt.dhbwaswepersonaldigitalagentmswetter.client;

import online.dhbw_studentprojekt.dhbwaswepersonaldigitalagentmswetter.dto.Wetter;
import org.springframework.web.client.RestClient;
import org.springframework.web.service.annotation.PostExchange;

public interface WetterClient extends RestClient {

        @PostExchange("/data/2.5/weather?q=Stuttgart&units=metric&appid=system.getenv('API_KEY')")
        Wetter getWetter();
}
