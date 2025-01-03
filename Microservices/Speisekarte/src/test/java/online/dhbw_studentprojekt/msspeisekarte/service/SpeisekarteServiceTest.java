package online.dhbw_studentprojekt.msspeisekarte.service;

import online.dhbw_studentprojekt.msspeisekarte.client.SpeisekarteClient;
import online.dhbw_studentprojekt.dto.speisekarte.Speisekarte;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class SpeisekarteServiceTest {


    @Mock
    private SpeisekarteClient speisekarteClient;

    @InjectMocks
    private SpeisekarteService speisekarteService;

    @BeforeEach
    void setUp() {
        initMockHtml();
        initSpeisekarte();
    }

    private Speisekarte speisekarte;
    private String mockHtml;
    private void initMockHtml(){
        mockHtml = "";
        Path filePath = Paths.get("src/test/resources/response_for_25_10.html");
        try {
            mockHtml = Files.readString(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initSpeisekarte(){
        speisekarte = new Speisekarte(
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
    }

    @Test
    void testGetSpeisekarte_ValidWeekday() {
        // Arrange
        String testDate = "2024-10-25"; // a valid weekday date

        // Mocking Client Behavior
        when(speisekarteClient.getSpeisekarte(any())).thenReturn(mockHtml);

        // Act
        Speisekarte result = speisekarteService.getSpeisekarte(Optional.of(testDate));

        // Assert
        assertNotNull(result, "Speisekarte should not be null for a weekday");
        verify(speisekarteClient, times(1)).getSpeisekarte(any());
        assertEquals(speisekarte, result);
    }

    @Test
    void testGetSpeisekarte_WeekendDate() {
        // Arrange
        String weekendDate = "2024-10-26"; // a weekend day date

        // Act & Assert
        ResponseStatusException exception = assertThrows(ResponseStatusException.class,
                () -> speisekarteService.getSpeisekarte(Optional.of(weekendDate)),
                "Expected ResponseStatusException for weekend dates");

        assertEquals("404 NOT_FOUND \"Am Wochenende gibt es keine Speisekarte\"", exception.getMessage());
        verify(speisekarteClient, never()).getSpeisekarte(any());
    }

    @Test
    void testGetSpeisekarteWithFilteredAllergene() {
        // Arrange
        String testDate = "2024-10-25";
        List<String> allergensToFilter = List.of("S"); // filter out Speisen containing "S"

        Speisekarte expectedFilteredSpeisekarte = new Speisekarte(
                // filter out "Rote Linsensuppe"
                List.of(),
                // VeganerRenner - no change
                speisekarte.veganerRenner(),
                // Hauptgericht - no change
                speisekarte.hauptgericht(),
                // filter out "Gebratene Kartoffeln"
                speisekarte.beilagen().subList(0,1),
                // Salat, Dessert, and Buffet - no change as none contain "S"
                speisekarte.salat(),
                speisekarte.dessert(),
                speisekarte.buffet()
        );

        // Mocking Client Behavior
        when(speisekarteClient.getSpeisekarte(any())).thenReturn(mockHtml);

        // Act
        Speisekarte result = speisekarteService.getSpeisekarteWithFilteredAllergene(Optional.of(testDate), allergensToFilter);

        // Assert
        assertNotNull(result, "Speisekarte should not be null for a weekday with allergen filtering");
        verify(speisekarteClient, times(1)).getSpeisekarte(any());
        assertEquals(expectedFilteredSpeisekarte, result, "Filtered Speisekarte does not match expected result");
    }

    @Test
    void testGetSpeisekarteWithEmptyAllergenList() {
        // Arrange
        String testDate = "2024-10-25";
        List<String> emptyAllergens = List.of(); // No allergens to filter

        // Mocking Client Behavior
        when(speisekarteClient.getSpeisekarte(any())).thenReturn(mockHtml);

        // Act
        Speisekarte result = speisekarteService.getSpeisekarteWithFilteredAllergene(Optional.of(testDate), emptyAllergens);

        // Assert
        assertNotNull(result, "Speisekarte should not be null for a weekday with no allergen filtering");
        verify(speisekarteClient, times(1)).getSpeisekarte(any());
        assertEquals(speisekarte, result, "Speisekarte should be unfiltered when allergen list is empty");
    }

    @Test
    void testGetSpeisekarteWithNullAllergenList() {
        // Arrange
        String testDate = "2024-10-25";
        // Mocking Client Behavior
        when(speisekarteClient.getSpeisekarte(any())).thenReturn(mockHtml);

        // Act
        Speisekarte result = speisekarteService.getSpeisekarteWithFilteredAllergene(Optional.of(testDate), null);

        // Assert
        assertNotNull(result, "Speisekarte should not be null for a weekday with no allergen filtering");
        verify(speisekarteClient, times(1)).getSpeisekarte(any());
        assertEquals(speisekarte, result, "Speisekarte should be unfiltered when allergen list is empty");
    }

    @Test
    void testGetSpeisekarteWithMultipleAllergens() {
        // Arrange
        String testDate = "2024-10-25";
        List<String> allergens = List.of("Gl", "S"); // Multiple allergens

        // Mocking Client Behavior
        when(speisekarteClient.getSpeisekarte(any())).thenReturn(mockHtml);

        // Act
        Speisekarte result = speisekarteService.getSpeisekarteWithFilteredAllergene(Optional.of(testDate), allergens);

        // Assert
        assertNotNull(result, "Speisekarte should not be null for a weekday");
        verify(speisekarteClient, times(1)).getSpeisekarte(any());

        // Ensure dishes containing either "Gl" or "S" are filtered out
        assertTrue(result.vorspeisen().stream().noneMatch(s -> s.allergene().contains("Gl") || s.allergene().contains("S")),
                "Vorspeisen should not contain any dishes with Gl or S allergens");
        assertTrue(result.veganerRenner().stream().noneMatch(s -> s.allergene().contains("Gl") || s.allergene().contains("S")),
                "VeganerRenner should not contain any dishes with Gl or S allergens");
        assertTrue(result.hauptgericht().stream().noneMatch(s -> s.allergene().contains("Gl") || s.allergene().contains("S")),
                "Hauptgericht should not contain any dishes with Gl or S allergens");
        assertTrue(result.beilagen().stream().noneMatch(s -> s.allergene().contains("Gl") || s.allergene().contains("S")),
                "Beilagen should not contain any dishes with Gl or S allergens");
        assertTrue(result.salat().stream().noneMatch(s -> s.allergene().contains("Gl") || s.allergene().contains("S")),
                "Salat should not contain any dishes with Gl or S allergens");
        assertTrue(result.dessert().stream().noneMatch(s -> s.allergene().contains("Gl") || s.allergene().contains("S")),
                "Dessert should not contain any dishes with Gl or S allergens");
        assertTrue(result.buffet().stream().noneMatch(s -> s.allergene().contains("Gl") || s.allergene().contains("S")),
                "Buffet should not contain any dishes with Gl or S allergens");
    }

}