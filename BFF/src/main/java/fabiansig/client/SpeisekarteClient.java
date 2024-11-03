package fabiansig.client;


import online.dhbw_studentprojekt.dto.speisekarte.Speisekarte;
import online.dhbw_studentprojekt.dto.speisekarte.SpeisekarteAllergeneRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface SpeisekarteClient {
    @GetExchange("api/speisekarte")
    Speisekarte getSpeisekarte(@RequestParam String datum);

    @PostExchange("api/speisekarte/allergene")
    Speisekarte getSpeisekarteWithFilteredAllergene(@RequestParam String datum, @RequestBody SpeisekarteAllergeneRequest allergene);
}
