package fabiansig.client;


import online.dhbw_studentprojekt.dto.speisekarte.Speisekarte;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.PostExchange;

public interface SpeisekarteClient {
    @PostExchange("api/speisekarte")
    Speisekarte getSpeisekarte(@RequestParam String date);
}
