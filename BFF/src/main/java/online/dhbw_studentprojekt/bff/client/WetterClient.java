package online.dhbw_studentprojekt.bff.client;

import online.dhbw_studentprojekt.dto.rapla.RaplaResponse;
import online.dhbw_studentprojekt.dto.wetter.Wetter;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;

public interface WetterClient {

    @GetExchange("/api/wetter")
    Wetter getWetter(@RequestParam("city") String city);
}
