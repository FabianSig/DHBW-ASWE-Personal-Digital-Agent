package online.dhbw_studentprojekt.bff.service;

import online.dhbw_studentprojekt.bff.client.*;
import online.dhbw_studentprojekt.dto.chatgpt.morning.MorningRequest;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatGPTMessage;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatGPTResponseChoice;
import online.dhbw_studentprojekt.dto.chatgpt.standard.ChatMessageRequest;
import online.dhbw_studentprojekt.dto.news.Article;
import online.dhbw_studentprojekt.dto.prefs.Preference;
import online.dhbw_studentprojekt.dto.speisekarte.Speisekarte;
import online.dhbw_studentprojekt.dto.stock.Stock;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BFFRoutineServiceTest {

    private final PrefsClient prefsClient = mock(PrefsClient.class);
    private final StockClient stockClient = mock(StockClient.class);
    private final ChatGPTClient chatGPTClient = mock(ChatGPTClient.class);
    private final SpeisekarteClient speisekarteClient = mock(SpeisekarteClient.class);
    private final NewsClient newsClient = mock(NewsClient.class);
    private final MapsClient mapsClient = mock(MapsClient.class);
    private final ContactsClient contactsClient = mock(ContactsClient.class);
    private final WetterClient wetterClient = mock(WetterClient.class);
    private final RoutineService routineService = new RoutineService(
            prefsClient, stockClient, chatGPTClient, speisekarteClient, newsClient, mapsClient, contactsClient, wetterClient);

    @Test
    void testGetMorningRoutine() {
        // Arrange
        when(prefsClient.getPreference("news-topics"))
                .thenReturn(Optional.of(new Preference("news-topics", List.of("Technology"))));
        when(prefsClient.getPreference("news-count"))
                .thenReturn(Optional.of(new Preference("news-count", List.of("3"))));
        when(prefsClient.getPreference("stock-symbols"))
                .thenReturn(Optional.of(new Preference("stock-symbols", List.of("AAPL", "TSLA", "AMZN"))));

        when(newsClient.getNews("Technology", 3))
                .thenReturn(List.of(
                        new Article(new Article.Source("1", "Title 1"), "author1", "Title 1", "Description 1", "/url/to/article1", "url/to/image1", "20.11.2024"),
                        new Article(new Article.Source("2", "Title 2"), "author2", "Title 2", "Description 2", "/url/to/article2", "url/to/image2", "20.11.2024"),
                        new Article(new Article.Source("3", "Title 3"), "author3", "Title 3", "Description 3", "/url/to/article3", "url/to/image3", "20.11.2024")
                ));
        when(stockClient.getMultipleStock(List.of("AAPL", "TSLA", "AMZN")))
                .thenReturn(List.of(
                        new Stock("AAPL", new Stock.DataPoint("2024-11-07", "150.0", "155.0", "149.0", "153.0"),
                                new Stock.DataPoint("2024-11-06", "145.0", "152.0", "144.0", "150.0")),
                        new Stock("TSLA", new Stock.DataPoint("2024-11-07", "150.0", "155.0", "149.0", "153.0"),
                                new Stock.DataPoint("2024-11-06", "145.0", "152.0", "144.0", "150.0"))
                ));

        // Use Argument Matchers
        when(chatGPTClient.getMorningRoutine(any(MorningRequest.class)))
                .thenReturn(new ChatGPTResponseChoice(new ChatGPTMessage("assistant", "Good morning! Here's your summary.")));

        // Act
        String routine = routineService.getMorningRoutine();

        // Assert
        assertEquals("Good morning! Here's your summary.", routine);

        // Verify interactions
        verify(prefsClient).getPreference("news-topics");
        verify(prefsClient).getPreference("news-count");
        verify(prefsClient).getPreference("stock-symbols");
        verify(newsClient).getNews("Technology", 3);
        verify(stockClient).getMultipleStock(List.of("AAPL", "TSLA", "AMZN"));
        verify(chatGPTClient).getMorningRoutine(any(MorningRequest.class));
    }


    @Test
    void testGetMittagRoutine() {
        // Arrange
        LocalDate today = LocalDate.now();
        if (today.getDayOfWeek().getValue() > 5) {
            today = today.plusDays(7L - today.getDayOfWeek().getValue());
        }

        when(prefsClient.getPreference("allergene"))
                .thenReturn(Optional.of(new Preference("allergene", List.of("nuts", "gluten"))));

        Speisekarte expectedSpeisekarte = new Speisekarte(
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
        when(speisekarteClient.getSpeisekarteWithFilteredAllergene(today.toString(), List.of("nuts", "gluten")))
                .thenReturn(expectedSpeisekarte);

        String prompt = "Bitte begrüße mich da es Mittagszeit ist und gebe ein mögliches Menü für das Mittagessen aus.";
        ChatMessageRequest expectedChatRequest = new ChatMessageRequest(prompt, "Speisekarte:" + expectedSpeisekarte);

        when(chatGPTClient.getResponse(expectedChatRequest, "routine", "message"))
                .thenReturn(new ChatGPTResponseChoice(new ChatGPTMessage("assistant","Enjoy your lunch! Here's the menu.")));

        // Act
        String routine = routineService.getMittagRoutine();

        // Assert
        assertEquals("Enjoy your lunch! Here's the menu.", routine);

        // Verify interactions
        verify(prefsClient).getPreference("allergene");
        verify(speisekarteClient).getSpeisekarteWithFilteredAllergene(eq(today.toString()), Mockito.anyList());
        verify(chatGPTClient).getResponse(expectedChatRequest, "routine", "message");
    }
}

