package online.dhbw_studentprojekt.mswetter.service;

import online.dhbw_studentprojekt.mswetter.client.WetterClient;
import online.dhbw_studentprojekt.dto.wetter.Wetter;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class WetterService {

    private final WetterClient wetterClient;

    public WetterService(WetterClient wetterClient) {
        this.wetterClient = wetterClient;
    }

    public Wetter getWetter() {
        return wetterClient.getWetter();
    }

}
