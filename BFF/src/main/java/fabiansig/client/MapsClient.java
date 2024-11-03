package fabiansig.client;

import online.dhbw_studentprojekt.dto.routing.custom.RouteAddressRequest;
import online.dhbw_studentprojekt.dto.routing.routing.RouteResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface MapsClient {
    @PostExchange("api/routing/address")
    RouteResponse getRouting(@RequestBody RouteAddressRequest request);
}