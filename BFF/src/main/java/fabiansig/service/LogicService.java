package fabiansig.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fabiansig.client.ChatGPTClient;
import fabiansig.client.MapsClient;
import fabiansig.dto.IntentionResponse;
import fabiansig.dto.MessageRequest;
import fabiansig.dto.MessageResponse;
import fabiansig.dto.RouteAddressRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LogicService {

    private final ChatGPTClient chatGPTClient;
    private final MapsClient mapsClient;

    public String sendMessage(MessageRequest message) {

        IntentionResponse intResponse = chatGPTClient.getIntention(message.content());

        Map<String, String> attributes = new java.util.HashMap<>();

        intResponse.attributes().forEach(attr -> attributes.put(attr.name(), attr.value()));
        Object data;

        if (intResponse.route().equals("api/routing/address")) {
            RouteAddressRequest routeAddressRequest = new RouteAddressRequest(attributes.get("origin"), attributes.get("destination"), attributes.get("travelMode"));
            data =  mapsClient.getRouting(routeAddressRequest);
        }

        String sekunden = "60s";




    }

}
