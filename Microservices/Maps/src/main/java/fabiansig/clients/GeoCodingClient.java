package fabiansig.clients;

import fabiansig.dto.geocoding.GeoCodingResponse;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.PostExchange;

public interface GeoCodingClient {

    @PostExchange("maps/api/geocode/json")
    GeoCodingResponse getGeoCoding(@RequestParam("address") String address,
                                     @RequestParam("key") String apiKey);

}
