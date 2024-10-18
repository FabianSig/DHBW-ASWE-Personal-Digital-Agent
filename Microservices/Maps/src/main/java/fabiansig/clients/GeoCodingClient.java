package fabiansig.clients;

import fabiansig.dto.geocoding.GeoCodingResponse;
import fabiansig.dto.routing.RouteRequest;
import fabiansig.dto.routing.RouteResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.GetExchange;
import org.springframework.web.service.annotation.PostExchange;

public interface GeoCodingClient {

    @PostExchange("maps/api/geocode/json")
    GeoCodingResponse getGeoCoding(@RequestParam("address") String address,
                                     @RequestParam("key") String apiKey);

}
