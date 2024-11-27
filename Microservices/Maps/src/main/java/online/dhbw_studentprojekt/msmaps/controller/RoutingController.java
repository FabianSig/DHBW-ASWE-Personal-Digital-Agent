package online.dhbw_studentprojekt.msmaps.controller;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import online.dhbw_studentprojekt.dto.routing.custom.DirectionResponse;
import online.dhbw_studentprojekt.dto.routing.custom.RouteAddressRequest;
import online.dhbw_studentprojekt.dto.routing.routing.RouteRequest;
import online.dhbw_studentprojekt.dto.routing.routing.RouteResponse;
import online.dhbw_studentprojekt.msmaps.service.RoutingService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routing")
@RequiredArgsConstructor
public class RoutingController {

    private final RoutingService routingService;

    @PostMapping("/route")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get the route between two points")
    public RouteResponse getRouting(@RequestBody RouteRequest request) {

        return routingService.getRoute(request);
    }

    @PostMapping("/address")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get the route between two addresses")
    public RouteResponse getRoutingByAddress(@RequestBody RouteAddressRequest request) {

        return routingService.getRouteByAddress(request);
    }

    @PostMapping("/directions")
    @ResponseStatus(HttpStatus.OK)
    @Operation(summary = "Get the directions between two addresses")
    DirectionResponse getDirections(@RequestBody RouteAddressRequest request) {

        return routingService.getDirections(request);
    }


}
