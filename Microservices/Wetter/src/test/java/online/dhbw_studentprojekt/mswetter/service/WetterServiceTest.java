package online.dhbw_studentprojekt.mswetter.service;

import online.dhbw_studentprojekt.dto.wetter.Wetter;
import online.dhbw_studentprojekt.dto.wetter.Wetter.Main;
import online.dhbw_studentprojekt.dto.wetter.Wetter.Weather;
import online.dhbw_studentprojekt.mswetter.client.WetterClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class WetterServiceTest {
    @Mock
    private WetterClient wetterClient;

    @InjectMocks
    private WetterService wetterService;

    @Test
    void testGetWetter() {
        // Arrange
        Main main = new Main(25.0, 23.5, 18.0, 28.0);
        Weather weather = new Weather(501, "Rain", "moderate rain", "10d");
        List<Weather> weatherList = List.of(weather);

        Wetter mockWetter = new Wetter("Stuttgart", main, weatherList);
        when(wetterClient.getWetter("Stuttgart")).thenReturn(mockWetter);

        // Act
        Wetter result = wetterService.getWetter("Stuttgart");

        // Assert
        assertNotNull(result);
        assertEquals("Stuttgart", result.name());
        assertEquals(25.0, result.main().temp());
        assertNotNull(result.weather());
        assertEquals(1, result.weather().size());
        assertEquals("Rain", result.weather().getFirst().main());
        assertEquals("moderate rain", result.weather().getFirst().description());
        verify(wetterClient, times(1)).getWetter("Stuttgart");
    }
}
