package online.dhbw_studentprojekt.dhbwaswepersonaldigitalagentmswetter.service;

import online.dhbw_studentprojekt.dhbwaswepersonaldigitalagentmswetter.client.WetterClient;
import online.dhbw_studentprojekt.dhbwaswepersonaldigitalagentmswetter.dto.Wetter;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WetterService {

    private final WetterClient wetterClient;

    public WetterService(WetterClient wetterClient) {
        this.wetterClient = wetterClient;
    }

    public Wetter getWetter(Optional<String> datumParam) {
        return wetterClient.getWetter();
    }

}
