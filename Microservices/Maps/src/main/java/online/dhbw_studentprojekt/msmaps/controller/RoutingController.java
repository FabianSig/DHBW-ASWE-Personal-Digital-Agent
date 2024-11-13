package online.dhbw_studentprojekt.msmaps.controller;

import online.dhbw_studentprojekt.dto.routing.custom.DirectionResponse;
import online.dhbw_studentprojekt.msmaps.service.RoutingService;
import lombok.RequiredArgsConstructor;
import online.dhbw_studentprojekt.dto.routing.custom.RouteAddressRequest;
import online.dhbw_studentprojekt.dto.routing.routing.RouteRequest;
import online.dhbw_studentprojekt.dto.routing.routing.RouteResponse;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/routing")
@RequiredArgsConstructor
public class RoutingController {

    private final RoutingService routingService;

    @PostMapping("/route")
    @ResponseStatus(HttpStatus.OK)
    public RouteResponse getRouting(@RequestBody RouteRequest request) {

        return routingService.getRoute(request);
    }

    @PostMapping("/address")
    @ResponseStatus(HttpStatus.OK)
    public RouteResponse getRoutingByAddress(@RequestBody RouteAddressRequest request) {

        return routingService.getRouteByAddress(request);
    }

    @PostMapping("/directions")
    DirectionResponse getDirections(@RequestBody RouteAddressRequest request) {

        return routingService.getDirections(request);
    }


}
