package fabiansig.client;

import fabiansig.dto.RouteAddressRequest;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.PostExchange;

public interface MapsClient {
    @PostExchange("api/routing/address")
    String getRouting(@RequestBody RouteAddressRequest request);
}
