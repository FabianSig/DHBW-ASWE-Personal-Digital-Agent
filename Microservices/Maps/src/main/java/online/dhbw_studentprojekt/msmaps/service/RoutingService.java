package online.dhbw_studentprojekt.msmaps.service;

import online.dhbw_studentprojekt.dto.routing.custom.DirectionResponse;
import online.dhbw_studentprojekt.msmaps.clients.MapsClient;
import online.dhbw_studentprojekt.msmaps.clients.RoutingClient;
import online.dhbw_studentprojekt.dto.routing.custom.RouteAddressRequest;
import online.dhbw_studentprojekt.dto.routing.geocoding.GeoCodingResponse;
import online.dhbw_studentprojekt.dto.routing.geocoding.Result;
import online.dhbw_studentprojekt.dto.routing.routing.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class RoutingService {

    private final RoutingClient routingClient;
    private final MapsClient mapsClient;
    private final GeoCodingService geoCodingService;

    public RouteResponse getRouteByAddress(RouteAddressRequest routeAddressRequest) {

        GeoCodingResponse originResponse = geoCodingService.getGeoCoding(routeAddressRequest.origin());
        GeoCodingResponse destinationResponse = geoCodingService.getGeoCoding(routeAddressRequest.destination());

        String travelMode = routeAddressRequest.travelMode();

        RouteRequest routeRequest = new RouteRequest(
                new Origin(extractCoordinates(originResponse)),
                new Destination(extractCoordinates(destinationResponse)),
                travelMode,
                travelMode.equals("TRANSIT") || travelMode.equals("WALK") || travelMode.equals("BICYCLE") ? null : "TRAFFIC_UNAWARE",
                false,
                new RouteModifiers(false, false, false),
                "en-US",
                "METRIC"
        );

        return this.getRoute(routeRequest);
    }

    public RouteResponse getRoute(RouteRequest routeRequest) {

        return routingClient.getRoute(routeRequest);
    }

    private static LatLng extractCoordinates(GeoCodingResponse response) {

        if (response.results() != null && !response.results().isEmpty()) {
            Result result = response.results().getFirst();
            return new LatLng(new Location(result.geometry().location().lat(), result.geometry().location().lng()));
        }
        throw new IllegalStateException("No results found in geocoding response.");
    }

    public DirectionResponse getDirections(RouteAddressRequest request) {

        GeoCodingResponse originResponse = geoCodingService.getGeoCoding(request.origin());
        GeoCodingResponse destinationResponse = geoCodingService.getGeoCoding(request.destination());
        DirectionResponse response = mapsClient.getDirections(extractPlaceId(originResponse),
                extractPlaceId(destinationResponse),
                System.getenv("API_KEY"),
                request.travelMode());

        return response;
    }

    private String extractPlaceId(GeoCodingResponse response) {

        if (response.results() != null && !response.results().isEmpty()) {
            Result result = response.results().getFirst();
            return "place_id:" + result.place_id();
        }
        throw new IllegalStateException("No results found in geocoding response.");
    }

}
