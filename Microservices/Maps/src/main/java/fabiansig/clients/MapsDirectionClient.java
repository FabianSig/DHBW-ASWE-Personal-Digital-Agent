package fabiansig.clients;

import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.service.annotation.PostExchange;

public interface MapsDirectionClient {

    @PostExchange("maps/api/directions/json")
    String getDirections(@RequestParam String origin,
                         @RequestParam String destination,
                         @RequestParam String key,
                         @RequestParam String mode);
}
