package fabiansig.service;

import fabiansig.clients.RoutingClient;
import fabiansig.dto.routing.RouteRequest;
import fabiansig.dto.routing.RouteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoutingService {


    private final RoutingClient routingClient;

    public RouteResponse getRoute(RouteRequest routeRequest) {
        return routingClient.getRoute(routeRequest);
    }
}
