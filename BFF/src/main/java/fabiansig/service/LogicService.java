package fabiansig.service;

import fabiansig.client.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.dhbw_studentprojekt.dto.chatgpt.intention.ChatGPTIntentionResponse;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatGPTResponseChoice;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatMessageRequest;
import online.dhbw_studentprojekt.dto.chatgpt.standard.MessageRequest;
import online.dhbw_studentprojekt.dto.routing.custom.RouteAddressRequest;
import online.dhbw_studentprojekt.dto.routing.routing.RouteResponse;
import online.dhbw_studentprojekt.dto.speisekarte.Speisekarte;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class LogicService {

    private final ChatGPTClient chatGPTClient;
    private final MapsClient mapsClient;
    private final SpeisekarteClient speisekarteClient;
    private final PrefsClient prefsClient;


    public String sendResponseMessage(MessageRequest message) {

        ChatGPTIntentionResponse intResponse = chatGPTClient.getIntention(message.message());

        Map<String, String> attributes = new java.util.HashMap<>();

        intResponse.attributes().forEach(attr -> attributes.put(attr.name().toLowerCase(), attr.value()));

        if (intResponse.route().equals("/api/routing/address")) {
            return getResponseMessageForRoutingAddressRequest(message, attributes);
        }
        if(intResponse.route().equals("/api/logic/speisekarte")){
            return getResponseMessageForSpeisekarteRequest(message, attributes);
        }

        return "Entschuldigung, das habe ich nicht verstanden. Bitte versuche es erneut.";
    }

    private String getResponseMessageForSpeisekarteRequest(MessageRequest message, Map<String, String> attributes) {

        try {
            String date = attributes.get("date");
            if(date == null){
                date= new Date().toString();
            }

            List<String> allergene = new ArrayList<>(prefsClient.getPreference("allergene").value());

            Speisekarte speisekarte = speisekarteClient.getSpeisekarteWithFilteredAllergene(date, allergene);

            if (speisekarte == null) {
                log.error("Speisekarte is null.");
                return "Error: Could not retrieve speisekarte information.";
            }

            ChatMessageRequest chatRequest = new ChatMessageRequest(message.message(),
                    "Speisekarte:" + speisekarte);
            ChatGPTResponseChoice gptResponse = chatGPTClient.getResponse(chatRequest, "test");

            return gptResponse.message().content();
        } catch (Exception e) {
            log.error("Error during speisekarte request: ", e);
            return "Error: Could not process speisekarte information.";
        }
    }

    private String getResponseMessageForRoutingAddressRequest(MessageRequest message, Map<String, String> attributes) {
        try {
            String origin = attributes.get("origin");
            String destination = attributes.get("destination");

            if (origin == null || destination == null) {
                log.error("Origin or destination is null. Origin: {}, Destination: {}", origin, destination);
                return "Bitte gebe einen gültigen Start- bzw Zielort an.";
            }
            RouteAddressRequest routeAddressRequest = new RouteAddressRequest(origin, destination, "TRANSIT");
            RouteResponse response = mapsClient.getRouting(routeAddressRequest);

            if (response == null || response.routes().isEmpty()) {
                log.error("Maps API response is empty or null.");
                return "Error: Could not retrieve routing information.";
            }

            ChatMessageRequest chatRequest = new ChatMessageRequest(message.message(),
                    "time to get there " + response.routes().getFirst().duration());
            ChatGPTResponseChoice gptResponse = chatGPTClient.getResponse(chatRequest, "test");

            if (gptResponse == null || gptResponse.message() == null) {
                log.error("ChatGPT response or message is null.");
                return "Error: Failed to retrieve response from ChatGPT.";
            }

            return gptResponse.message().content();
        } catch (Exception e) {
            log.error("Error during routing request: ", e);
            return "Error: Could not process routing information.";
        }
    }
}
