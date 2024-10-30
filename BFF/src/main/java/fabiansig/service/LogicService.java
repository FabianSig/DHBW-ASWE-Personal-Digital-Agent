package fabiansig.service;

import fabiansig.client.ChatGPTClient;
import fabiansig.client.MapsClient;
import online.dhbw_studentprojekt.dto.chatgpt.intention.ChatGPTIntentionResponse;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatGPTResponseChoice;
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

        intResponse.attributes().forEach(attr -> attributes.put(attr.name().toLowerCase(), attr.value()));

        if (intResponse.route().equals("/api/routing/address")) {

            RouteAddressRequest routeAddressRequest = new RouteAddressRequest(attributes.get("origin"), attributes.get("destination"), "TRANSIT");

            RouteResponse response =  mapsClient.getRouting(routeAddressRequest);

            ChatMessageRequest request = new ChatMessageRequest(message.message(), "time to get there " + response.routes().getFirst().duration());

            ChatGPTResponseChoice gptResponse = chatGPTClient.getResponse(request, "test");

            return gptResponse.message().content();
        }

        return "Entschuldigung, das habe ich nicht verstanden. Bitte versuche es erneut.";
    }

}
