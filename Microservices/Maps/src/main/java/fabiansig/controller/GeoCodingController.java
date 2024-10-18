package fabiansig.controller;

import fabiansig.dto.geocoding.GeoCodingResponse;
import fabiansig.dto.routing.RouteRequest;
import fabiansig.dto.routing.RouteResponse;
import fabiansig.service.GeoCodingService;
import fabiansig.service.RoutingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/geocoding")
@RequiredArgsConstructor
public class GeoCodingController {

    private final GeoCodingService geoCodingService;

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public GeoCodingResponse getRouting(@RequestParam String address) {
        return geoCodingService.getGeoCoding(address);
    }
}
