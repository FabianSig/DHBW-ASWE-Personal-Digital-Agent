package online.dhbw_studentprojekt.bff.service;

import online.dhbw_studentprojekt.bff.client.ChatGPTClient;
import online.dhbw_studentprojekt.bff.client.MapsClient;
import online.dhbw_studentprojekt.bff.client.PrefsClient;
import online.dhbw_studentprojekt.bff.client.SpeisekarteClient;
import online.dhbw_studentprojekt.dto.chatgpt.intention.ChatGPTIntentionAttribute;
import online.dhbw_studentprojekt.dto.chatgpt.intention.ChatGPTIntentionResponse;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatGPTMessage;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatGPTResponseChoice;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatMessageRequest;
import online.dhbw_studentprojekt.dto.chatgpt.standard.MessageRequest;
import online.dhbw_studentprojekt.dto.prefs.Preference;
import online.dhbw_studentprojekt.dto.routing.custom.DirectionResponse;
import online.dhbw_studentprojekt.dto.routing.custom.RouteAddressRequest;
import online.dhbw_studentprojekt.dto.speisekarte.Speisekarte;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BFFIntentionMessageServiceTest {

    @InjectMocks
    private IntentionMessageService intentionMessageService;

    @Mock
    private ChatGPTClient chatGPTClient;

    @Mock
    private MapsClient mapsClient;

    @Mock
    private SpeisekarteClient speisekarteClient;

    @Mock
    private PrefsClient prefsClient;

    @Test
    void testGetResponseMessage_withRoutingAddress() {

        MessageRequest messageRequest = new MessageRequest("How do I get from A to B?");
        ChatGPTIntentionResponse intentionResponse = new ChatGPTIntentionResponse("/api/routing/address",
                List.of(new ChatGPTIntentionAttribute("origin", "A"),
                        new ChatGPTIntentionAttribute("destination", "B"),
                        new ChatGPTIntentionAttribute("travelmode", "driving")));

        when(chatGPTClient.getIntention(messageRequest.message())).thenReturn(intentionResponse);

        RouteAddressRequest expectedRouteRequest = new RouteAddressRequest("A", "B", "driving");

        DirectionResponse.Time arrivalTime = new DirectionResponse.Time("12:30", "UTC", 45000L);  // 12:30 (in seconds from midnight)
        DirectionResponse.Time departureTime = new DirectionResponse.Time("12:00", "UTC", 43200L); // 12:00 (in seconds from midnight)
        DirectionResponse.Distance distance = new DirectionResponse.Distance("10 km", 10000); // 10 km in meters
        DirectionResponse.Duration duration = new DirectionResponse.Duration("30 mins", 1800); // 30 minutes in seconds
        List<DirectionResponse.Step> steps = List.of();  // Empty steps list

        DirectionResponse.Leg leg = new DirectionResponse.Leg(arrivalTime, departureTime, distance, duration, "Destination B", "Origin A", steps);

        DirectionResponse.Route route = new DirectionResponse.Route(List.of(leg));

        DirectionResponse mockDirectionResponse = new DirectionResponse(List.of(route));

        when(mapsClient.getDirections(expectedRouteRequest)).thenReturn(mockDirectionResponse);

        when(chatGPTClient.getResponse(any(ChatMessageRequest.class), any(), eq("maps")))
                .thenReturn(new ChatGPTResponseChoice(new ChatGPTMessage("assistant", "Here is the route information.")));

        String response = intentionMessageService.getResponseMessage(messageRequest);

        Assertions.assertEquals("Here is the route information.", response);

        verify(chatGPTClient).getIntention(messageRequest.message());
        verify(mapsClient).getDirections(expectedRouteRequest);
        verify(chatGPTClient).getResponse(any(ChatMessageRequest.class), any(), eq("maps"));
    }

    @Test
    void testGetResponseMessage_withSpeisekarte() {

        MessageRequest messageRequest = new MessageRequest("What's on the menu?");
        ChatGPTIntentionResponse intentionResponse = new ChatGPTIntentionResponse("/api/logic/speisekarte",
                List.of(new ChatGPTIntentionAttribute("date", "2024-11-20")));
        when(chatGPTClient.getIntention(messageRequest.message())).thenReturn(intentionResponse);

        List<String> mockAllergene = List.of("nuts", "gluten");
        Speisekarte mockSpeisekarte = new Speisekarte(
                // vorspeisen
                List.of(
                        new Speisekarte.Speise(
                                "Rote Linsensuppe",
                                List.of("3", "S"),
                                List.of(
                                        new Speisekarte.Speise.Naehrwerte("Brennwert", "844.9 kj / 202.0 kcal"),
                                        new Speisekarte.Speise.Naehrwerte("Fett", "0.8 g"),
                                        new Speisekarte.Speise.Naehrwerte("Kohlenhydrate", "30.6 g"),
                                        new Speisekarte.Speise.Naehrwerte("Eiweiß", "14.1 g"),
                                        new Speisekarte.Speise.Naehrwerte("Salz", "0.6 g")
                                )
                        )
                ),
                // veganerRenner
                List.of(
                        new Speisekarte.Speise(
                                "Gemüse-Knusperfrikadelle mit veganer Kräutercremesauce und Reis",
                                List.of("So", "Sl", "GlW", "Gl"),
                                List.of(
                                        new Speisekarte.Speise.Naehrwerte("Brennwert", "2265.7 kj / 541.0 kcal"),
                                        new Speisekarte.Speise.Naehrwerte("Fett", "25.7 g"),
                                        new Speisekarte.Speise.Naehrwerte("Kohlenhydrate", "67.6 g"),
                                        new Speisekarte.Speise.Naehrwerte("Eiweiß", "8.8 g"),
                                        new Speisekarte.Speise.Naehrwerte("Salz", "1.2 g")
                                )
                        )
                ),
                // hauptgericht
                List.of(
                        new Speisekarte.Speise(
                                "Waldpilzragout mit Kartoffelplätzchen und Sour-Cream Dip",
                                List.of("La", "Sl", "Gl"),
                                List.of(
                                        new Speisekarte.Speise.Naehrwerte("Brennwert", "2923.4 kj / 698.0 kcal"),
                                        new Speisekarte.Speise.Naehrwerte("Fett", "44.4 g"),
                                        new Speisekarte.Speise.Naehrwerte("Kohlenhydrate", "53.8 g"),
                                        new Speisekarte.Speise.Naehrwerte("Eiweiß", "16.3 g"),
                                        new Speisekarte.Speise.Naehrwerte("Salz", "3.8 g")
                                )
                        ),
                        new Speisekarte.Speise(
                                "Backfisch mit Frankfurter Grüne Sauce und Salzkartoffeln",
                                List.of("Ei", "La", "Sf", "Sw", "Gl"),
                                List.of(
                                        new Speisekarte.Speise.Naehrwerte("Brennwert", "3971.9 kj / 949.0 kcal"),
                                        new Speisekarte.Speise.Naehrwerte("Fett", "47.4 g"),
                                        new Speisekarte.Speise.Naehrwerte("Kohlenhydrate", "75.0 g"),
                                        new Speisekarte.Speise.Naehrwerte("Eiweiß", "50.1 g"),
                                        new Speisekarte.Speise.Naehrwerte("Salz", "2.7 g")
                                )
                        )
                ),
                // beilagen
                List.of(
                        new Speisekarte.Speise(
                                "Broccoli",
                                List.of("nbs"),
                                List.of(
                                        new Speisekarte.Speise.Naehrwerte("Brennwert", "238.2 kj / 57.0 kcal"),
                                        new Speisekarte.Speise.Naehrwerte("Fett", "0.0 g"),
                                        new Speisekarte.Speise.Naehrwerte("Kohlenhydrate", "4.1 g"),
                                        new Speisekarte.Speise.Naehrwerte("Eiweiß", "6.0 g"),
                                        new Speisekarte.Speise.Naehrwerte("Salz", "1.0 g")
                                )
                        ),
                        new Speisekarte.Speise(
                                "Gebratene Kartoffeln",
                                List.of("S"),
                                List.of(
                                        new Speisekarte.Speise.Naehrwerte("Brennwert", "729.1 kj / 174.0 kcal"),
                                        new Speisekarte.Speise.Naehrwerte("Fett", "0.3 g"),
                                        new Speisekarte.Speise.Naehrwerte("Kohlenhydrate", "35.1 g"),
                                        new Speisekarte.Speise.Naehrwerte("Eiweiß", "4.5 g"),
                                        new Speisekarte.Speise.Naehrwerte("Salz", "0.8 g")
                                )
                        )
                ),
                // salat
                List.of(
                        new Speisekarte.Speise(
                                "Beilagensalat",
                                List.of("nbs"),
                                List.of(
                                        new Speisekarte.Speise.Naehrwerte("Brennwert", "7.3 kj / 2.0 kcal"),
                                        new Speisekarte.Speise.Naehrwerte("Fett", "0.0 g"),
                                        new Speisekarte.Speise.Naehrwerte("Kohlenhydrate", "0.2 g"),
                                        new Speisekarte.Speise.Naehrwerte("Eiweiß", "0.1 g"),
                                        new Speisekarte.Speise.Naehrwerte("Salz", "0.0 g")
                                )
                        )
                ),
                // dessert
                List.of(
                        new Speisekarte.Speise(
                                "Nuss-Nougat-Pudding",
                                List.of("La", "Nu"),
                                List.of(
                                        new Speisekarte.Speise.Naehrwerte("Brennwert", "1233.7 kj / 295.0 kcal"),
                                        new Speisekarte.Speise.Naehrwerte("Fett", "8.8 g"),
                                        new Speisekarte.Speise.Naehrwerte("Kohlenhydrate", "46.2 g"),
                                        new Speisekarte.Speise.Naehrwerte("Eiweiß", "7.1 g"),
                                        new Speisekarte.Speise.Naehrwerte("Salz", "0.6 g")
                                )
                        )
                ),
                // buffet
                List.of(
                        new Speisekarte.Speise(
                                "Salatbuffet",
                                List.of("2", "3", "5", "8", "10", "Ei", "La", "Sl", "Sf", "Se", "Sw", "Gl"),
                                List.of(
                                        new Speisekarte.Speise.Naehrwerte("Brennwert", "9999.9 kj / 999.9 kcal"),
                                        new Speisekarte.Speise.Naehrwerte("Fett", "691.6 g"),
                                        new Speisekarte.Speise.Naehrwerte("Kohlenhydrate", "352.2 g"),
                                        new Speisekarte.Speise.Naehrwerte("Eiweiß", "78.0 g"),
                                        new Speisekarte.Speise.Naehrwerte("Salz", "36.3 g")
                                )
                        )
                )
        );
        when(prefsClient.getPreference("allergene")).thenReturn(Optional.of(new Preference("allergene", mockAllergene)));
        when(speisekarteClient.getSpeisekarteWithFilteredAllergene("2024-11-20", mockAllergene)).thenReturn(mockSpeisekarte);

        when(chatGPTClient.getResponse(any(ChatMessageRequest.class), any(), eq("message")))
                .thenReturn(new ChatGPTResponseChoice(new ChatGPTMessage("assistant", "Here is the menu.")));

        String response = intentionMessageService.getResponseMessage(messageRequest);

        Assertions.assertEquals("Here is the menu.", response);

        verify(chatGPTClient).getIntention(messageRequest.message());
        verify(prefsClient).getPreference("allergene");
        verify(speisekarteClient).getSpeisekarteWithFilteredAllergene("2024-11-20", mockAllergene);
        verify(chatGPTClient).getResponse(any(ChatMessageRequest.class), any(), eq("message"));
    }

    @Test
    void testGetResponseMessage_withSpeisekarteError() {

        MessageRequest messageRequest = new MessageRequest("What's on the menu?");
        ChatGPTIntentionResponse intentionResponse = new ChatGPTIntentionResponse("/api/logic/speisekarte",
                List.of(new ChatGPTIntentionAttribute("date", null)));
        when(chatGPTClient.getIntention(messageRequest.message())).thenReturn(intentionResponse);

        List<String> mockAllergene = List.of("nuts", "gluten");

        when(prefsClient.getPreference("allergene")).thenReturn(Optional.of(new Preference("allergene", mockAllergene)));
        when(speisekarteClient.getSpeisekarteWithFilteredAllergene("2024-11-20", mockAllergene)).thenReturn(null);

        when(chatGPTClient.getResponse(any(ChatMessageRequest.class), any(), eq("message")))
                .thenReturn(new ChatGPTResponseChoice(new ChatGPTMessage("assistant", "Here is the menu.")));

        // assert that getResponseMessage throws org.springframework. web. server. ResponseStatusException: 500 INTERNAL_SERVER_ERROR "Could not process Speisekarte information."

        Assertions.assertThrows(ResponseStatusException.class, () -> intentionMessageService.getResponseMessage(messageRequest));

    }

    @Test
    void testGetResponseMessage_withUnknownRoute() {

        MessageRequest messageRequest = new MessageRequest("Unknown request");
        ChatGPTIntentionResponse intentionResponse = new ChatGPTIntentionResponse("/api/unknown", List.of());

        when(chatGPTClient.getIntention(messageRequest.message())).thenReturn(intentionResponse);

        String response = intentionMessageService.getResponseMessage(messageRequest);

        Assertions.assertEquals("Entschuldigung, das habe ich nicht verstanden. Bitte versuche es erneut.", response);

        verify(chatGPTClient).getIntention(messageRequest.message());
        verifyNoInteractions(mapsClient, speisekarteClient, prefsClient);
    }

}
