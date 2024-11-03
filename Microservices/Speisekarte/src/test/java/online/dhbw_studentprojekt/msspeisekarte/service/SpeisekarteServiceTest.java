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
    void testGetSpeisekarte_NoDateProvided() {
        // Arrange
        when(speisekarteClient.getSpeisekarte(any())).thenReturn(mockHtml);

        // Act
        Speisekarte result = speisekarteService.getSpeisekarte(Optional.empty());

        // Assert
        assertNotNull(result, "Speisekarte should not be null when no date is provided");
        verify(speisekarteClient, times(1)).getSpeisekarte(any());
        assertEquals(speisekarte, result);
    }

    // The following tests for prepareFormData Method
    // Commented out because of private method
    // Decide later if needed
    // either refactor to be public method or test indirectly
/*
    @Test
    void testPrepareFormData_Weekday() {
        // Arrange
        String testDate = "2024-10-29";
        MultiValueMap<String, String> formData = SpeisekarteService.prepareFormData(testDate);

        // Assert
        assertEquals("make_spl", formData.getFirst("func"));
        assertEquals("16", formData.getFirst("locId"));
        assertEquals("de", formData.getFirst("lang"));
        assertEquals(testDate, formData.getFirst("date"));
        assertNotNull(formData.getFirst("startThisWeek"));
        assertNotNull(formData.getFirst("startNextWeek"));
    }

    @Test
    void testPrepareFormData_EdgeCase() {
        // Arrange
        String testDate = "2024-01-01"; // A Monday at the start of the year
        MultiValueMap<String, String> formData = SpeisekarteService.prepareFormData(testDate);

        // Assert
        assertEquals("make_spl", formData.getFirst("func"));
        assertEquals("16", formData.getFirst("locId"));
        assertEquals("de", formData.getFirst("lang"));
        assertEquals(testDate, formData.getFirst("date"));
        assertNotNull(formData.getFirst("startThisWeek"));
        assertNotNull(formData.getFirst("startNextWeek"));
    }
 */
}