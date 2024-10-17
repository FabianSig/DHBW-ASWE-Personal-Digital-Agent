package fabiansig;

import fabiansig.dto.RouteRequest;
import fabiansig.dto.RouteResponse;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface GoogleClient {

    @PostExchange("/directions/v2:computeRoutes")
    RouteResponse getRoute(@RequestBody RouteRequest request);
}
