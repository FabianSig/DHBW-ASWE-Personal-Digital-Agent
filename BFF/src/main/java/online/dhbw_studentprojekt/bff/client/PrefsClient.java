package online.dhbw_studentprojekt.bff.client;

import online.dhbw_studentprojekt.dto.prefs.Preference;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

import java.util.Optional;

public interface PrefsClient {

    @GetExchange(url = "/api/prefs")
    Optional<Preference> getPreference(@RequestParam String id);


    @PostExchange(url = "/api/prefs")
    void createPreference(@RequestBody Preference preference);

}
