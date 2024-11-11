package online.dhbw_studentprojekt.msmaps.service;


import online.dhbw_studentprojekt.dto.routing.geocoding.*;
import online.dhbw_studentprojekt.msmaps.clients.MapsClient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class GeoCodingServiceTest {

    @Mock
    private MapsClient geoCodingClient;

    @InjectMocks
    private GeoCodingService geoCodingService;
    private GeoCodingResponse geoCodingResponse;

    @BeforeEach
    void setUp() {

        initGeoCodingResponse();
    }

    private void initGeoCodingResponse() {

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
    void testGetGeoCoding() {

        when(geoCodingClient.getGeoCoding(any(), any())).thenReturn(geoCodingResponse);

        // Act
        GeoCodingResponse response = geoCodingService.getGeoCoding("1600 Amphitheatre Parkway");

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.status()).isEqualTo("OK");
        assertThat(response.results()).hasSize(1);

        Result returnedResult = response.results().get(0);
        assertThat(returnedResult.formatted_address()).isEqualTo("1600 Amphitheatre Parkway, Mountain View, CA, USA");
        assertThat(returnedResult.geometry().location().lat()).isEqualTo(37.423021);
        assertThat(returnedResult.geometry().location().lng()).isEqualTo(-122.083739);
        verify(geoCodingClient, times(1)).getGeoCoding(any(), any());
    }

}
