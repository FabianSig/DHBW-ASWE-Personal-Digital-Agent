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

    /**
     * Retrieves a route between two addresses based on the provided travel mode and route preferences.

     *
     * @param routeAddressRequest an object containing the origin and destination addresses,
     *                            as well as the desired travel mode (e.g., "DRIVING", "WALKING")
     * @return a {@link RouteResponse} object containing the route details such as distance, duration,
     *         and path
     */

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

    /**
     * Sends a route request to the routing client and retrieves the route details.
     *
     * @param routeRequest a {@link RouteRequest} object containing the necessary parameters
     *                     to calculate the route, such as destination and travel mode
     * @return a {@link RouteResponse} object containing details of the calculated route,
     *         including distance, duration, and the route path
     */
    public RouteResponse getRoute(RouteRequest routeRequest) {

        return routingClient.getRoute(routeRequest);
    }

    /**
     * Retrieves directions between two addresses based on the provided travel mode.
     *
     * @param request a {@link RouteAddressRequest} object containing the destination
     *                addresses, as well as the desired travel mode (e.g., "DRIVING", "WALKING")
     * @return a {@link DirectionResponse} object containing detailed direction information,
     *         such as step-by-step instructions, distance, and duration
     */
    public DirectionResponse getDirections(RouteAddressRequest request) {

        GeoCodingResponse originResponse = geoCodingService.getGeoCoding(request.origin());
        GeoCodingResponse destinationResponse = geoCodingService.getGeoCoding(request.destination());
        DirectionResponse response = mapsClient.getDirections(extractPlaceId(originResponse),
                extractPlaceId(destinationResponse),
                System.getenv("API_KEY"),
                request.travelMode());

        return response;
    }

    /**
     * Extracts the place ID from a geocoding response.
     *
     * @param response a {@link GeoCodingResponse} object containing the geocoding results
     * @return a String representing the place ID
     */
    private String extractPlaceId(GeoCodingResponse response) {

        if (response.results() != null && !response.results().isEmpty()) {
            Result result = response.results().getFirst();
            return "place_id:" + result.place_id();
        }
        throw new IllegalStateException("No results found in geocoding response.");
    }

    /**
     * Extracts the coordinates (latitude and longitude) from a geocoding response.
     *
     * @param geoCodingResponse a {@link GeoCodingResponse} object containing the geocoding results
     */
    private static LatLng extractCoordinates(GeoCodingResponse geoCodingResponse) {

        if (geoCodingResponse.results() != null && !geoCodingResponse.results().isEmpty()) {
            Result result = geoCodingResponse.results().getFirst();
            return new LatLng(new Location(result.geometry().location().lat(), result.geometry().location().lng()));
        }
        throw new IllegalStateException("No results found in geocoding response.");
    }

}
