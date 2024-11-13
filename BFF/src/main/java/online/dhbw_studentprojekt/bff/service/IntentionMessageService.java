package online.dhbw_studentprojekt.bff.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.dhbw_studentprojekt.bff.client.*;
import online.dhbw_studentprojekt.dto.chatgpt.intention.ChatGPTIntentionResponse;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatGPTResponseChoice;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatMessageRequest;
import online.dhbw_studentprojekt.dto.chatgpt.standard.MessageRequest;
import online.dhbw_studentprojekt.dto.prefs.Preference;
import online.dhbw_studentprojekt.dto.routing.custom.RouteAddressRequest;
import online.dhbw_studentprojekt.dto.routing.routing.RouteResponse;
import online.dhbw_studentprojekt.dto.speisekarte.Speisekarte;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class IntentionMessageService {

    private final ChatGPTClient chatGPTClient;
    private final MapsClient mapsClient;
    private final SpeisekarteClient speisekarteClient;
    private final PrefsClient prefsClient;

    /**
     * Processes the given message and generates a response based on the detected intention.
     *
     * @param message the message to process
     * @return the generated response message
     */
    public String sendResponseMessage(MessageRequest message) {

        ChatGPTIntentionResponse intResponse = chatGPTClient.getIntention(message.message());

        Map<String, String> attributes = new java.util.HashMap<>();

        intResponse.attributes().forEach(attr -> attributes.put(attr.name().toLowerCase(), attr.value()));

        return switch (intResponse.route()) {
            case "/api/routing/address" -> getResponseMessageForRoutingAddressRequest(message, attributes);
            case "/api/logic/speisekarte" -> getResponseMessageForSpeisekarteRequest(message, attributes);
            default -> "Entschuldigung, das habe ich nicht verstanden. Bitte versuche es erneut.";
        };
    }

    /**
     * Processes the given message and generates a response based on the detected intention.
     *
     * @param message the message to process
     * @return the generated response message
     */
    private String getResponseMessageForSpeisekarteRequest(MessageRequest message, Map<String, String> attributes) {

        try {
            String date = attributes.get("date");
            if (date == null) {
                date = LocalDate.now().toString();
            }

            List<String> allergene = prefsClient.getPreference("allergene")
                    .map(Preference::value)
                    .orElse(Collections.emptyList());

            Speisekarte speisekarte = speisekarteClient.getSpeisekarteWithFilteredAllergene(date, allergene);

            if (speisekarte == null) {
                log.error("Speisekarte is null.");
                return "Error: Could not retrieve speisekarte information.";
            }

            ChatMessageRequest chatRequest = new ChatMessageRequest(message.message(),
                    "Speisekarte:" + speisekarte);
            ChatGPTResponseChoice gptResponse = chatGPTClient.getResponse(chatRequest, "test", "message");

            return gptResponse.message().content();
        } catch (Exception e) {
            log.error("Error during speisekarte request: ", e);
            return "Error: Could not process speisekarte information.";
        }
    }

    /**
     * Generates a response message for a routing address request.
     *
     * @param message    the original message
     * @param attributes the attributes extracted from the intention response
     * @return the generated response message
     */
    private String getResponseMessageForRoutingAddressRequest(MessageRequest message, Map<String, String> attributes) {

        final String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        try {
            // Extract data and check for faulty input
            String origin = attributes.get("origin");
            String destination = attributes.get("destination");
            String travelMode = attributes.get("travelmode");

            if (origin == null || destination == null || travelMode == null) {
                log.error("Origin or destination is null. Origin: {}, Destination: {}", origin, destination);
                return "Bitte gebe einen gültigen Start- bzw Zielort an.";
            }

            // Get routing information and check for faulty response
            RouteResponse response = mapsClient.getRouting(new RouteAddressRequest(origin, destination, travelMode.toUpperCase()));
            String directionResponse = mapsClient.getDirections(new RouteAddressRequest(origin, destination, travelMode.toLowerCase()));

            if (response == null || response.routes().isEmpty()) {
                log.error("Maps API response is empty or null.");
                return "Error: Could not retrieve routing information.";
            }

            // Generate message to return to user
            ChatMessageRequest chatRequest = new ChatMessageRequest(message.message(),
                    "time to get there " + response.routes().getFirst().duration() + "\n"
                            + "current Time: " + currentTime + "\n"
                            + "additional data about the route: " + directionResponse);

            ChatGPTResponseChoice gptResponse = chatGPTClient.getResponse(chatRequest, "test", "maps");

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
