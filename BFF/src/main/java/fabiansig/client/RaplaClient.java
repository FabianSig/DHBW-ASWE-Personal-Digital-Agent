package fabiansig.client;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import online.dhbw_studentprojekt.dto.rapla.RaplaResponse;

import java.util.List;

public interface RaplaClient {

    @GetExchange("/")
    RaplaResponse getLectureTimes(@RequestParam("date") String date);
}
