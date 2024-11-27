package online.dhbw_studentprojekt.bff.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import online.dhbw_studentprojekt.bff.client.ChatGPTClient;
import online.dhbw_studentprojekt.bff.client.MapsClient;
import online.dhbw_studentprojekt.bff.client.PrefsClient;
import online.dhbw_studentprojekt.bff.client.SpeisekarteClient;
import online.dhbw_studentprojekt.dto.chatgpt.intention.ChatGPTIntentionResponse;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatGPTResponseChoice;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatId;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatMessageRequest;
import online.dhbw_studentprojekt.dto.chatgpt.standard.MessageRequest;
import online.dhbw_studentprojekt.dto.prefs.Preference;
import online.dhbw_studentprojekt.dto.routing.custom.DirectionResponse;
import online.dhbw_studentprojekt.dto.routing.custom.RouteAddressRequest;
import online.dhbw_studentprojekt.dto.speisekarte.Speisekarte;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

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
    public String getResponseMessage(MessageRequest message) {

        ChatGPTIntentionResponse intentionResponse = chatGPTClient.getIntention(message.message());

        Map<String, String> attributes = new java.util.HashMap<>();

        intentionResponse.attributes().forEach(attr -> attributes.put(attr.name().toLowerCase(), attr.value()));

        return switch (intentionResponse.route()) {
            case "/api/routing/address" -> getRoutingResponse(message, attributes);
            case "/api/logic/speisekarte" -> getSpeisekarteResponse(message, attributes);
            default -> "Entschuldigung, das habe ich nicht verstanden. Bitte versuche es erneut.";
        };
    }

    /**
     * Generates a response message for a routing address request.
     *
     * @param message    the original message
     * @param attributes the attributes extracted from the intention response
     * @return the generated response message
     */
    private String getRoutingResponse(MessageRequest message, Map<String, String> attributes) {

        final String currentTime = LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        try {
            // Extract data and check for faulty input
            String origin = attributes.get("origin");
            String destination = attributes.get("destination");
            String travelMode = attributes.get("travelmode");

            if (origin == null || destination == null || travelMode == null) {
                log.error("origin, destination oder travelMode is null. Origin: {}, Destination: {}, travelMode: {}", origin, destination, travelMode);
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Keine gültigen Start- bzw Zielort angegeben");
            }

            // Get routing information and check for faulty response
            DirectionResponse directionResponse = mapsClient.getDirections(new RouteAddressRequest(origin, destination, travelMode.toLowerCase()));

            if (directionResponse == null || directionResponse.routes().isEmpty()) {
                log.error("Maps API response is empty or null.");
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR);
            }

            // Request for user-facing message
            ChatMessageRequest chatRequest = new ChatMessageRequest(message.message(),
                    "time to get there " + directionResponse.routes().getFirst().legs().getFirst().duration().text() + "\n"
                            + "current Time: " + currentTime + "\n"
                            + "additional data about the route: " + directionResponse);

            ChatGPTResponseChoice gptResponse = chatGPTClient.getResponse(chatRequest, ChatId.TEST.getValue(), "maps");

            if (gptResponse == null || gptResponse.message() == null) {
                log.error("ChatGPT response or message is null.");
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to retrieve response from ChatGPT");
            }

            return gptResponse.message().content();
        } catch (Exception e) {
            log.error("Error during routing request: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not process routing information.");
        }
    }

    /**
     * Processes the given message and generates a response based on the detected intention.
     *
     * @param message the message to process
     * @return the generated response message
     */
    private String getSpeisekarteResponse(MessageRequest message, Map<String, String> attributes) {

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
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Speisekarte nicht gefunden.");
            }

            ChatMessageRequest chatRequest = new ChatMessageRequest(message.message(),
                    "Speisekarte:" + speisekarte);
            ChatGPTResponseChoice gptResponse = chatGPTClient.getResponse(chatRequest, ChatId.TEST.getValue(), "message");

            return gptResponse.message().content();
        } catch (Exception e) {
            log.error("Error during speisekarte request: ", e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Could not process Speisekarte information.");
        }
    }

}
