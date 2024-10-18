package fabiansig.service;

import fabiansig.GoogleClient;
import fabiansig.dto.routing.RouteRequest;
import fabiansig.dto.routing.RouteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoutingService {


    private final GoogleClient googleClient;

    public RouteResponse getRoute(RouteRequest routeRequest) {


        return googleClient.getRoute(routeRequest);
    }
}
