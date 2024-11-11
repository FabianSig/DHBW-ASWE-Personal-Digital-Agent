package online.dhbw_studentprojekt.mswetter.controller;

import online.dhbw_studentprojekt.dto.wetter.Wetter;
import online.dhbw_studentprojekt.dto.wetter.Wetter.Main;
import online.dhbw_studentprojekt.mswetter.config.SecurityConfig;
import online.dhbw_studentprojekt.mswetter.service.WetterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import java.util.Optional;
import static org.mockito.Mockito.*;

@WebMvcTest(WetterController.class)
@Import(SecurityConfig.class)
public class WetterControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private WetterService wetterService;

    @Test
    void testGetWetter() throws Exception {
        // Arrange
        Main main = new Main(20.5, 19.0, 15.0, 22.0);
        Wetter mockWetter = new Wetter("Stuttgart", main);
        when(wetterService.getWetter()).thenReturn(mockWetter);

        // Act & Assert
        mockMvc.perform(get("/api/wetter"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Stuttgart"))
                .andExpect(jsonPath("$.main.temp").value(20.5))
                .andExpect(jsonPath("$.main.feels_like").value(19.0))
                .andExpect(jsonPath("$.main.temp_min").value(15.0))
                .andExpect(jsonPath("$.main.temp_max").value(22.0));

        verify(wetterService, times(1)).getWetter();
    }
}
