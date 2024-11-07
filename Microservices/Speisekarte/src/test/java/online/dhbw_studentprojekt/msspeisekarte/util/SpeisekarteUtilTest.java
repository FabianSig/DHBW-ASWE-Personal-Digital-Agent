package online.dhbw_studentprojekt.msspeisekarte.util;

import online.dhbw_studentprojekt.dto.speisekarte.Speisekarte;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
public class SpeisekarteUtilTest {
    // Tests the extractMenu Method also covers the complete chain of private Methods in the utils class
    @Test
    public void testExtractMenu_ParsesHtmlCorrectly() {
        // Arrange
        // loading the HTML file as a String
        Path filePath = Paths.get("src/test/resources/example_html_response.html");
        String html = "";
        try {
            html = Files.readString(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Expected Speisekarte object
        Speisekarte expectedMenu = new Speisekarte(
                // vorspeisen
                List.of(
                        new Speisekarte.Speise(
                                "Blumenkohlsuppe",
                                List.of("La", "Sl", "Gl"),
                                List.of(
                                        new Speisekarte.Speise.Naehrwerte("Brennwert", "716.4 kj / 171.0 kcal"),
                                        new Speisekarte.Speise.Naehrwerte("Fett", "12.4 g"),
                                        new Speisekarte.Speise.Naehrwerte("Kohlenhydrate", "10.3 g"),
                                        new Speisekarte.Speise.Naehrwerte("Eiweiß", "4.5 g"),
                                        new Speisekarte.Speise.Naehrwerte("Salz", "0.6 g")
                                )
                        )
                ),
                // veganerRenner
                List.of(
                        new Speisekarte.Speise(
                                "Kartoffel-Gemüse-Pfanne",
                                List.of("S"),
                                List.of(
                                        new Speisekarte.Speise.Naehrwerte("Brennwert", "2109.5 kj / 504.0 kcal"),
                                        new Speisekarte.Speise.Naehrwerte("Fett", "19.5 g"),
                                        new Speisekarte.Speise.Naehrwerte("Kohlenhydrate", "55.0 g"),
                                        new Speisekarte.Speise.Naehrwerte("Eiweiß", "17.1 g"),
                                        new Speisekarte.Speise.Naehrwerte("Salz", "1.5 g")
                                )
                        )
                ),
                // hauptgericht
                List.of(
                        new Speisekarte.Speise(
                                "Cevapcici mit Djuvec-Reis und Ajvar",
                                List.of("Ei", "Sl", "Gl"),
                                List.of(
                                        new Speisekarte.Speise.Naehrwerte("Brennwert", "2012.7 kj / 481.0 kcal"),
                                        new Speisekarte.Speise.Naehrwerte("Fett", "18.5 g"),
                                        new Speisekarte.Speise.Naehrwerte("Kohlenhydrate", "54.2 g"),
                                        new Speisekarte.Speise.Naehrwerte("Eiweiß", "20.6 g"),
                                        new Speisekarte.Speise.Naehrwerte("Salz", "2.9 g")
                                )
                        ),
                        new Speisekarte.Speise(
                                "Paniertes Schweineschnitzel mit Bratensauce und Pommes frites",
                                List.of("Ei", "Gl"),
                                List.of(
                                        new Speisekarte.Speise.Naehrwerte("Brennwert", "4187.7 kj / 999.9 kcal"),
                                        new Speisekarte.Speise.Naehrwerte("Fett", "56.7 g"),
                                        new Speisekarte.Speise.Naehrwerte("Kohlenhydrate", "82.3 g"),
                                        new Speisekarte.Speise.Naehrwerte("Eiweiß", "37.2 g"),
                                        new Speisekarte.Speise.Naehrwerte("Salz", "3.9 g")
                                )
                        )
                ),
                // beilagen
                List.of(
                        new Speisekarte.Speise(
                                "Fitness Bowl mit Hähnchen und Hirtenkäse",
                                List.of("2", "5", "Ei", "La", "S"),
                                List.of(
                                        new Speisekarte.Speise.Naehrwerte("Brennwert", "2724.2 kj / 651.0 kcal"),
                                        new Speisekarte.Speise.Naehrwerte("Fett", "46.3 g"),
                                        new Speisekarte.Speise.Naehrwerte("Kohlenhydrate", "13.3 g"),
                                        new Speisekarte.Speise.Naehrwerte("Eiweiß", "44.9 g"),
                                        new Speisekarte.Speise.Naehrwerte("Salz", "3.5 g")
                                )
                        )
                ),
                // salat
                List.of(
                        new Speisekarte.Speise(
                                "Chinagemüse",
                                List.of("S"),
                                List.of(
                                        new Speisekarte.Speise.Naehrwerte("Brennwert", "342.7 kj / 82.0 kcal"),
                                        new Speisekarte.Speise.Naehrwerte("Fett", "5.7 g"),
                                        new Speisekarte.Speise.Naehrwerte("Kohlenhydrate", "6.7 g"),
                                        new Speisekarte.Speise.Naehrwerte("Eiweiß", "4.7 g"),
                                        new Speisekarte.Speise.Naehrwerte("Salz", "1.9 g")
                                )
                        ),
                        new Speisekarte.Speise(
                                "Pommes frites",
                                List.of("nbs"),
                                List.of(
                                        new Speisekarte.Speise.Naehrwerte("Brennwert", "2095.0 kj / 500.0 kcal"),
                                        new Speisekarte.Speise.Naehrwerte("Fett", "31.9 g"),
                                        new Speisekarte.Speise.Naehrwerte("Kohlenhydrate", "46.2 g"),
                                        new Speisekarte.Speise.Naehrwerte("Eiweiß", "5.5 g"),
                                        new Speisekarte.Speise.Naehrwerte("Salz", "1.1 g")
                                )
                        )
                ),
                // dessert
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
                // buffet
                List.of(
                        new Speisekarte.Speise(
                                "Himbeerquark",
                                List.of("L"),
                                List.of(
                                        new Speisekarte.Speise.Naehrwerte("Brennwert", "935.2 kj / 223.0 kcal"),
                                        new Speisekarte.Speise.Naehrwerte("Fett", "0.8 g"),
                                        new Speisekarte.Speise.Naehrwerte("Kohlenhydrate", "25.6 g"),
                                        new Speisekarte.Speise.Naehrwerte("Eiweiß", "21.9 g"),
                                        new Speisekarte.Speise.Naehrwerte("Salz", "0.2 g")
                                )
                        )
                )
        );
        // Act
        Speisekarte result = SpeisekarteUtils.extractMenu(html);

        // Assert
        assertEquals(expectedMenu, result);
    }
    @Test
    public void testExtractMenu_No_Allergenes_ParsesHtmlCorrectly() {
        // Arrange
        // loading the HTML file as a String
        Path filePath = Paths.get("src/test/resources/example_html_response_no_allergenes.html");
        String html = "";
        try {
            html = Files.readString(filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Expected Speisekarte object
        Speisekarte expectedMenu = new Speisekarte(
                List.of(
                        // vorspeisen
                        new Speisekarte.Speise(
                                "Tagessuppe",
                                List.of(),
                                List.of(
                                        new Speisekarte.Speise.Naehrwerte("Brennwert", "0.0 kj / 172.0 kcal"),
                                        new Speisekarte.Speise.Naehrwerte("Fett", "0.0 g"),
                                        new Speisekarte.Speise.Naehrwerte("Kohlenhydrate", "0.0 g"),
                                        new Speisekarte.Speise.Naehrwerte("Eiweiß", "0.0 g"),
                                        new Speisekarte.Speise.Naehrwerte("Salz", "0.0 g")
                                )
                        )
                ),
                List.of(
                        // veganerRenner
                        new Speisekarte.Speise(
                                "Penne mit Champignons und Cocktailtomaten",
                                List.of("Gl"),
                                List.of(
                                        new Speisekarte.Speise.Naehrwerte("Brennwert", "2324.0 kj / 555.0 kcal"),
                                        new Speisekarte.Speise.Naehrwerte("Fett", "11.9 g"),
                                        new Speisekarte.Speise.Naehrwerte("Kohlenhydrate", "90.9 g"),
                                        new Speisekarte.Speise.Naehrwerte("Eiweiß", "17.5 g"),
                                        new Speisekarte.Speise.Naehrwerte("Salz", "2.0 g")
                                )
                        )
                ),
                List.of(
                        // hauptgericht
                        new Speisekarte.Speise(
                                "Rote Wurst mit Pusztasauce und Country Potatoes",
                                List.of("1", "3", "8", "12", "Sl", "Sf", "Gl"),
                                List.of(
                                        new Speisekarte.Speise.Naehrwerte("Brennwert", "4330.0 kj / 999.9 kcal"),
                                        new Speisekarte.Speise.Naehrwerte("Fett", "77.5 g"),
                                        new Speisekarte.Speise.Naehrwerte("Kohlenhydrate", "61.0 g"),
                                        new Speisekarte.Speise.Naehrwerte("Eiweiß", "21.6 g"),
                                        new Speisekarte.Speise.Naehrwerte("Salz", "7.5 g")
                                )
                        ),
                        new Speisekarte.Speise(
                                "Köttbullar mit Rahmsauce ,Preiselbeeren und Salzkartoffeln",
                                List.of("Ei", "La", "Sw", "Gl"),
                                List.of(
                                        new Speisekarte.Speise.Naehrwerte("Brennwert", "2702.9 kj / 646.0 kcal"),
                                        new Speisekarte.Speise.Naehrwerte("Fett", "26.1 g"),
                                        new Speisekarte.Speise.Naehrwerte("Kohlenhydrate", "72.5 g"),
                                        new Speisekarte.Speise.Naehrwerte("Eiweiß", "26.0 g"),
                                        new Speisekarte.Speise.Naehrwerte("Salz", "4.1 g")
                                )
                        )
                ),
                List.of(
                        // beilagen
                        new Speisekarte.Speise(
                                "Gemüsewok Grünes Thai Curry mit Duftreis",
                                List.of("F"),
                                List.of(
                                        new Speisekarte.Speise.Naehrwerte("Brennwert", "2529.4 kj / 604.0 kcal"),
                                        new Speisekarte.Speise.Naehrwerte("Fett", "8.6 g"),
                                        new Speisekarte.Speise.Naehrwerte("Kohlenhydrate", "110.0 g"),
                                        new Speisekarte.Speise.Naehrwerte("Eiweiß", "15.9 g"),
                                        new Speisekarte.Speise.Naehrwerte("Salz", "4.2 g")
                                )
                        ),
                        new Speisekarte.Speise(
                                "Hähnchenstreifen zum Wokgericht",
                                List.of("nbs"),
                                List.of(
                                        new Speisekarte.Speise.Naehrwerte("Brennwert", "492.0 kj / 118.0 kcal"),
                                        new Speisekarte.Speise.Naehrwerte("Fett", "1.9 g"),
                                        new Speisekarte.Speise.Naehrwerte("Kohlenhydrate", "1.4 g"),
                                        new Speisekarte.Speise.Naehrwerte("Eiweiß", "23.0 g"),
                                        new Speisekarte.Speise.Naehrwerte("Salz", "1.5 g")
                                )
                        )
                ),
                List.of(
                        // salat
                        new Speisekarte.Speise(
                                "Tagesgemüse",
                                List.of(),
                                List.of(
                                        new Speisekarte.Speise.Naehrwerte("Brennwert", "0.0 kj / 172.0 kcal"),
                                        new Speisekarte.Speise.Naehrwerte("Fett", "0.0 g"),
                                        new Speisekarte.Speise.Naehrwerte("Kohlenhydrate", "0.0 g"),
                                        new Speisekarte.Speise.Naehrwerte("Eiweiß", "0.0 g"),
                                        new Speisekarte.Speise.Naehrwerte("Salz", "0.0 g")
                                )
                        ),
                        new Speisekarte.Speise(
                                "Duftreis",
                                List.of("nbs"),
                                List.of(
                                        new Speisekarte.Speise.Naehrwerte("Brennwert", "1211.2 kj / 289.0 kcal"),
                                        new Speisekarte.Speise.Naehrwerte("Fett", "1.6 g"),
                                        new Speisekarte.Speise.Naehrwerte("Kohlenhydrate", "61.5 g"),
                                        new Speisekarte.Speise.Naehrwerte("Eiweiß", "5.8 g"),
                                        new Speisekarte.Speise.Naehrwerte("Salz", "0.5 g")
                                )
                        ),
                        new Speisekarte.Speise(
                                "Country Potatoes",
                                List.of("Gl"),
                                List.of(
                                        new Speisekarte.Speise.Naehrwerte("Brennwert", "2036.0 kj / 486.0 kcal"),
                                        new Speisekarte.Speise.Naehrwerte("Fett", "31.0 g"),
                                        new Speisekarte.Speise.Naehrwerte("Kohlenhydrate", "44.1 g"),
                                        new Speisekarte.Speise.Naehrwerte("Eiweiß", "5.5 g"),
                                        new Speisekarte.Speise.Naehrwerte("Salz", "3.6 g")
                                )
                        )
                ),
                List.of(
                        // dessert
                        new Speisekarte.Speise(
                                "Beilagensalat",
                                List.of("nbs"),
                                List.of(
                                        new Speisekarte.Speise.Naehrwerte("Brennwert", "41.5 kj / 10.0 kcal"),
                                        new Speisekarte.Speise.Naehrwerte("Fett", "0.1 g"),
                                        new Speisekarte.Speise.Naehrwerte("Kohlenhydrate", "1.1 g"),
                                        new Speisekarte.Speise.Naehrwerte("Eiweiß", "0.8 g"),
                                        new Speisekarte.Speise.Naehrwerte("Salz", "0.1 g")
                                )
                        )
                ),
                List.of(
                        // buffet
                        new Speisekarte.Speise(
                                "Kirschjoghurt",
                                List.of("2", "L"),
                                List.of(
                                        new Speisekarte.Speise.Naehrwerte("Brennwert", "721.8 kj / 172.0 kcal"),
                                        new Speisekarte.Speise.Naehrwerte("Fett", "5.6 g"),
                                        new Speisekarte.Speise.Naehrwerte("Kohlenhydrate", "22.5 g"),
                                        new Speisekarte.Speise.Naehrwerte("Eiweiß", "6.8 g"),
                                        new Speisekarte.Speise.Naehrwerte("Salz", "0.3 g")
                                )
                        )
                )
        );

        // Act
        Speisekarte result = SpeisekarteUtils.extractMenu(html);
        // Assert
        assertEquals(expectedMenu, result);
    }
}
