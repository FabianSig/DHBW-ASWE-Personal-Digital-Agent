package online.dhbw_studentprojekt.bff.client;


import online.dhbw_studentprojekt.dto.speisekarte.Speisekarte;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

import java.util.List;

public interface SpeisekarteClient {
    @GetExchange("api/speisekarte")
    Speisekarte getSpeisekarte(@RequestParam String datum);

    @GetExchange("api/speisekarte/allergene")
    Speisekarte getSpeisekarteWithFilteredAllergene(@RequestParam String datum, @RequestParam(required = false) List<String> allergene);
}
