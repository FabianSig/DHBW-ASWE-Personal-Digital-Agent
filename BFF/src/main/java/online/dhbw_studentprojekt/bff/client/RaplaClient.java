package online.dhbw_studentprojekt.bff.client;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import online.dhbw_studentprojekt.dto.rapla.RaplaResponse;

public interface RaplaClient {

    @GetExchange("/")
    RaplaResponse getLectureTimes(@RequestParam("date") String date);
}
