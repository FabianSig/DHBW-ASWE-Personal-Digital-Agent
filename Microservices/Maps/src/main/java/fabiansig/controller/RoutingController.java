package fabiansig.controller;

import fabiansig.dto.custom.RouteAddressRequest;
import fabiansig.dto.routing.RouteRequest;
import fabiansig.dto.routing.RouteResponse;
import fabiansig.service.RoutingService;
import lombok.RequiredArgsConstructor;
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


}
