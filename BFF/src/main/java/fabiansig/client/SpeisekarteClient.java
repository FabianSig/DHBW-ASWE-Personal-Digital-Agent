package fabiansig.client;


import online.dhbw_studentprojekt.dto.speisekarte.Speisekarte;
import online.dhbw_studentprojekt.dto.speisekarte.SpeisekarteAllergeneRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface SpeisekarteClient {
    @GetExchange("api/speisekarte")
    Speisekarte getSpeisekarte(@RequestParam String datum);

    @GetExchange("api/speisekarte/allergene")
    Speisekarte getSpeisekarteWithFilteredAllergene(@RequestParam String datum, @RequestBody SpeisekarteAllergeneRequest allergene);
}
