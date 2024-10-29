package nikomitk.personaldigitalagentmsspeisekarte.controller;

import nikomitk.personaldigitalagentmsspeisekarte.dto.Speisekarte;
import nikomitk.personaldigitalagentmsspeisekarte.service.SpeisekarteService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.server.ResponseStatusException;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*; // For .param


import java.util.List;
import java.util.Optional;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
public class SpeisekarteControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SpeisekarteService speisekarteService;

    @BeforeEach
    void setUp() {
        initSpeisekarte();
    }

    private Speisekarte speisekarte;

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
    void testGetSpeisekarte_ValidDate() throws Exception {
        // Arrange
        String testDate = "2024-10-25";

        when(speisekarteService.getSpeisekarte(Optional.of(testDate))).thenReturn(speisekarte);

        // Act & Assert
        mockMvc.perform(get("/api/speisekarte")
                        .param("datum", testDate)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                // Verify some fields in the JSON response using jsonPath -> no Object Mapper
                .andExpect(jsonPath("$.vorspeisen[0].name").value(speisekarte.vorspeisen().get(0).name()))
                .andExpect(jsonPath("$.veganerRenner[0].name").value(speisekarte.veganerRenner().get(0).name()))
                .andExpect(jsonPath("$.hauptgericht[0].name").value(speisekarte.hauptgericht().get(0).name()))
                .andExpect(jsonPath("$.beilagen[0].name").value(speisekarte.beilagen().get(0).name()))
                .andExpect(jsonPath("$.salat[0].name").value(speisekarte.salat().get(0).name()))
                .andExpect(jsonPath("$.dessert[0].name").value(speisekarte.dessert().get(0).name()))
                .andExpect(jsonPath("$.buffet[0].name").value(speisekarte.buffet().get(0).name()));
    }
    @Test
    void testGetSpeisekarte_NoDate() throws Exception {
        // Arrange
        when(speisekarteService.getSpeisekarte(Optional.empty())).thenReturn(speisekarte);

        // Act & Assert
        mockMvc.perform(get("/api/speisekarte")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.vorspeisen[0].name").value(speisekarte.vorspeisen().get(0).name()))
                .andExpect(jsonPath("$.veganerRenner[0].name").value(speisekarte.veganerRenner().get(0).name()))
                .andExpect(jsonPath("$.hauptgericht[0].name").value(speisekarte.hauptgericht().get(0).name()))
                .andExpect(jsonPath("$.beilagen[0].name").value(speisekarte.beilagen().get(0).name()))
                .andExpect(jsonPath("$.salat[0].name").value(speisekarte.salat().get(0).name()))
                .andExpect(jsonPath("$.dessert[0].name").value(speisekarte.dessert().get(0).name()))
                .andExpect(jsonPath("$.buffet[0].name").value(speisekarte.buffet().get(0).name()));
    }

    @Test
    void testGetSpeisekarte_WeekendDate() throws Exception {
        // Arrange
        String invalidDate = "2024-10-27"; // Weekend Date -> invalid date
        when(speisekarteService.getSpeisekarte(Optional.of(invalidDate))).thenThrow(new ResponseStatusException(HttpStatus.NOT_FOUND, "Am Wochenende gibt es keine Speisekarte"));

        // Act & Assert
        mockMvc.perform(get("/api/speisekarte")
                        .param("datum", invalidDate)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound()); // Expect a 404 Not Found
    }
}
