package fabiansig.client;

import online.dhbw_studentprojekt.dto.speisekarte.Speisekarte;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface RaplaClient {

    @GetExchange()
    Speisekarte getSpeisekarte(@RequestParam String date);
}
