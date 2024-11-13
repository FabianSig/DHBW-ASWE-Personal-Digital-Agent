package online.dhbw_studentprojekt.msmaps.clients;

import online.dhbw_studentprojekt.dto.routing.custom.DirectionResponse;
import online.dhbw_studentprojekt.dto.routing.geocoding.GeoCodingResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.PostExchange;

public interface MapsClient {

    @PostExchange("maps/api/geocode/json")
    GeoCodingResponse getGeoCoding(@RequestParam("address") String address,
                                     @RequestParam("key") String apiKey);

    @PostExchange("maps/api/directions/json")
    DirectionResponse getDirections(@RequestParam String origin,
                                    @RequestParam String destination,
                                    @RequestParam String key,
                                    @RequestParam String mode);

}
