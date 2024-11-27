package online.dhbw_studentprojekt.bff.client;

import online.dhbw_studentprojekt.dto.routing.custom.DirectionResponse;
import online.dhbw_studentprojekt.dto.routing.custom.RouteAddressRequest;
import online.dhbw_studentprojekt.dto.routing.geocoding.GeoCodingResponse;
import online.dhbw_studentprojekt.dto.routing.routing.RouteResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.PostExchange;

public interface MapsClient {
    @PostExchange("api/routing/address")
    RouteResponse getRouting(@RequestBody RouteAddressRequest request);

    @PostExchange("api/routing/directions")
    DirectionResponse getDirections(@RequestBody RouteAddressRequest request);

    @PostExchange("api/geocoding")
    GeoCodingResponse getGeoCoding(@RequestParam String address);
}
