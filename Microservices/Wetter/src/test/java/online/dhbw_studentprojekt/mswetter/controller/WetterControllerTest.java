package online.dhbw_studentprojekt.mswetter.controller;

import online.dhbw_studentprojekt.dto.wetter.Wetter;
import online.dhbw_studentprojekt.dto.wetter.Wetter.Main;
import online.dhbw_studentprojekt.dto.wetter.Wetter.Weather;
import online.dhbw_studentprojekt.mswetter.config.SecurityConfig;
import online.dhbw_studentprojekt.mswetter.service.WetterService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
        String city = "Stuttgart";
        Main main = new Main(20.5, 19.0, 15.0, 22.0);
        Weather weather = new Weather(501, "Rain", "moderate rain", "10d");
        List<Weather> weatherList = List.of(weather);

        Wetter mockWetter = new Wetter(city, main, weatherList);
        when(wetterService.getWetter(city)).thenReturn(mockWetter);

        // Act & Assert
        mockMvc.perform(get("/api/wetter").param("city", city))

                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(city))
                .andExpect(jsonPath("$.main.temp").value(20.5))
                .andExpect(jsonPath("$.main.feels_like").value(19.0))
                .andExpect(jsonPath("$.main.temp_min").value(15.0))
                .andExpect(jsonPath("$.main.temp_max").value(22.0))
                .andExpect(jsonPath("$.weather[0].id").value(501))
                .andExpect(jsonPath("$.weather[0].main").value("Rain"))
                .andExpect(jsonPath("$.weather[0].description").value("moderate rain"))
                .andExpect(jsonPath("$.weather[0].icon").value("10d"));

        verify(wetterService, times(1)).getWetter(city);
    }
}
