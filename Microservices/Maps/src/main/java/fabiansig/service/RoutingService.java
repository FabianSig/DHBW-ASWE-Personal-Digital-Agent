package fabiansig.service;

import fabiansig.clients.MapsClient;
import fabiansig.dto.custom.RouteAddressRequest;
import fabiansig.dto.geocoding.GeoCodingResponse;
import fabiansig.dto.geocoding.Result;
import fabiansig.dto.routing.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoutingService {


    private final MapsClient routingClient;
    private final GeoCodingService geoCodingService;

    public RouteResponse getRoute(RouteRequest routeRequest) {
        return routingClient.getRoute(routeRequest);
    }

    public RouteResponse getRouteByAddress(RouteAddressRequest routeAddressRequest) {

        GeoCodingResponse originResponse = geoCodingService.getGeoCoding(routeAddressRequest.origin());
        GeoCodingResponse destinationResponse = geoCodingService.getGeoCoding(routeAddressRequest.destination());

        String travelMode = routeAddressRequest.travelMode();

        return this.getRoute(new RouteRequest(
                new Origin(extractCoordinates(originResponse)),
                new Destination(extractCoordinates(destinationResponse)),
                travelMode,
                travelMode.equals("TRANSIT") || travelMode.equals("WALK") || travelMode.equals("BICYCLE") ? null : "TRAFFIC_UNAWARE",
                false,
                new RouteModifiers(false, false, false),
                "en-US",
                "METRIC"
        ));
    }

    private static LatLng extractCoordinates(GeoCodingResponse response) {
        if (response.results() != null && !response.results().isEmpty()) {
            Result result = response.results().getFirst();
            return new LatLng(new Location(result.geometry().location().lat(), result.geometry().location().lng()));
        }
        throw new IllegalStateException("No results found in geocoding response.");
    }
}
