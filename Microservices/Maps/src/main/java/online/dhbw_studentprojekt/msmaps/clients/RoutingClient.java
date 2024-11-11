package online.dhbw_studentprojekt.msmaps.clients;

import online.dhbw_studentprojekt.dto.routing.routing.RouteRequest;
import online.dhbw_studentprojekt.dto.routing.routing.RouteResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface RoutingClient {

    @PostExchange("directions/v2:computeRoutes")
    RouteResponse getRoute(@RequestBody RouteRequest request);
}
