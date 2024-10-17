package fabiansig.controller;

import fabiansig.dto.RouteRequest;
import fabiansig.dto.RouteResponse;
import fabiansig.service.RoutingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/routing")
@RequiredArgsConstructor
public class RoutingController {

    private final RoutingService routingService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public String getRouting(@RequestBody RouteRequest request) {
        return "routingService.getRoute(request)";
    }
}
