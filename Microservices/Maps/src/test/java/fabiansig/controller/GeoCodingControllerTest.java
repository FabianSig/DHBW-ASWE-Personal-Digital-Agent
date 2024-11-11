package fabiansig.controller;


import fabiansig.dto.geocoding.*;
import fabiansig.service.GeoCodingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(GeoCodingController.class)
public class GeoCodingControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private GeoCodingService geoCodingService;

    @BeforeEach
    void setUp() {
        initGeoCodingResponse();
    }

    private GeoCodingResponse geoCodingResponse;

    private void initGeoCodingResponse(){
        Location location = new Location(37.423021, -122.083739);
        Viewport viewport = new Viewport(
                new Location(37.423021, -122.083739),
                new Location(37.421021, -122.085739)
        );
        Geometry geometry = new Geometry(location, "ROOFTOP", viewport);

        AddressComponent addressComponent = new AddressComponent("1600 Amphitheatre Parkway", "1600", List.of("street_address"));
        PlusCode plusCode = new PlusCode("849VCWC8+R9", "849VCWC8+R9");
        Result result = new Result(
                List.of(addressComponent),
                "1600 Amphitheatre Parkway, Mountain View, CA, USA",
                geometry,
                "ChIJ2eUgeAK6j4ARbn5u_wAGqWA",
                plusCode,
                List.of("establishment")
        );

        geoCodingResponse = new GeoCodingResponse(List.of(result), "OK");
    }

    @Test
    void TestGetGoeCoding() throws Exception {
        // Mock service behavior
        when(geoCodingService.getGeoCoding(anyString())).thenReturn(geoCodingResponse);

        // Act and Assert: Perform request and validate response
        mockMvc.perform(post("/api/geocoding")
                        .param("address", "1600 Amphitheatre Parkway"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("OK"))
                .andExpect(jsonPath("$.results[0].formatted_address").value("1600 Amphitheatre Parkway, Mountain View, CA, USA"))
                .andExpect(jsonPath("$.results[0].geometry.location.lat").value(37.423021))
                .andExpect(jsonPath("$.results[0].geometry.location.lng").value(-122.083739));

        // Verify interaction
        verify(geoCodingService).getGeoCoding("1600 Amphitheatre Parkway");
    }
}
