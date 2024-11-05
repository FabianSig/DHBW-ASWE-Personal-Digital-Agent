package online.dhbw_studentprojekt.mswetter.service;

import online.dhbw_studentprojekt.dto.wetter.Wetter;
import online.dhbw_studentprojekt.dto.wetter.Wetter.Main;
import online.dhbw_studentprojekt.mswetter.client.WetterClient;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.when;
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
        Wetter mockWetter = new Wetter("Stuttgart", main);
        when(wetterClient.getWetter()).thenReturn(mockWetter);

        // Act
        Wetter result = wetterService.getWetter(Optional.empty());

        // Assert
        assertNotNull(result);
        assertEquals("Stuttgart", result.name());
        assertEquals(25.0, result.main().temp());
        verify(wetterClient, times(1)).getWetter();
    }
}
