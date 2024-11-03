package fabiansig.clients;

import fabiansig.dto.routing.RouteRequest;
import fabiansig.dto.routing.RouteResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface MapsClient {

    @PostExchange("directions/v2:computeRoutes")
    RouteResponse getRoute(@RequestBody RouteRequest request);
}
