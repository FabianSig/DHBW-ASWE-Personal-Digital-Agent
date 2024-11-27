package online.dhbw_studentprojekt.mswetter.service;

import lombok.RequiredArgsConstructor;
import online.dhbw_studentprojekt.dto.wetter.Wetter;
import online.dhbw_studentprojekt.mswetter.client.WetterClient;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WetterService {

    private final WetterClient wetterClient;

    public Wetter getWetter(String city) {

        return wetterClient.getWetter(city);
    }

}
