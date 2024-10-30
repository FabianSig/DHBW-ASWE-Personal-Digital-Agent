package fabiansig.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fabiansig.client.ChatGPTClient;
import fabiansig.client.MapsClient;
import online.dhbw_studentprojekt.dto.chatgpt.intention.ChatGPTIntentionResponse;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatMessageRequest;
import online.dhbw_studentprojekt.dto.chatgpt.standard.MessageRequest;
import online.dhbw_studentprojekt.dto.routing.custom.RouteAddressRequest;
import online.dhbw_studentprojekt.dto.routing.routing.RouteResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class LogicService {

    private final ChatGPTClient chatGPTClient;
    private final MapsClient mapsClient;

    public String sendMessage(MessageRequest message) {

        ChatGPTIntentionResponse intResponse = chatGPTClient.getIntention(message.message());

        Map<String, String> attributes = new java.util.HashMap<>();

        intResponse.attributes().forEach(attr -> attributes.put(attr.name(), attr.value()));

        if (intResponse.route().equals("api/routing/address")) {
            RouteAddressRequest routeAddressRequest = new RouteAddressRequest(attributes.get("origin"), attributes.get("destination"), attributes.get("travelMode"));
              RouteResponse response =  mapsClient.getRouting(routeAddressRequest);

              ChatMessageRequest request = new ChatMessageRequest(message.message(), response.toString());

              return chatGPTClient.getResponse(request, "test").choices().getFirst().message().content();
        }
        return "nein";
    }

}
