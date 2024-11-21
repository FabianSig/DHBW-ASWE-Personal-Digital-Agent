package online.dhbw_studentprojekt.msmaps.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import online.dhbw_studentprojekt.dto.routing.custom.DirectionResponse;
import online.dhbw_studentprojekt.dto.routing.custom.RouteAddressRequest;
import online.dhbw_studentprojekt.dto.routing.routing.*;
import online.dhbw_studentprojekt.msmaps.service.RoutingService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(RoutingController.class)
class RoutingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RoutingService routingService;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Test
    void testGetRouting_Success() throws Exception {
        RouteRequest routeRequest = new RouteRequest(
                new Origin(new LatLng(new Location(10.0, 20.0))),
                new Destination(new LatLng(new Location(30.0, 40.0))),
                "CAR",
                "TRAFFIC_UNAWARE",
                false,
                new RouteModifiers(false, false, false),
                "en-US",
                "METRIC"
        );
        RouteResponse mockResponse = new RouteResponse(new ArrayList<>());

        Mockito.when(routingService.getRoute(Mockito.any(RouteRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/routing/route")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(routeRequest)))
                .andExpect(status().isOk());

                //.andExpect(jsonPath("$.fieldName").value("expectedValue"));

        Mockito.verify(routingService).getRoute(Mockito.any(RouteRequest.class));
    }

    @Test
    void testGetRoutingByAddress_Success() throws Exception {
        RouteAddressRequest addressRequest = new RouteAddressRequest("OriginAddress", "DestinationAddress", "CAR");
        RouteResponse mockResponse = new RouteResponse(new ArrayList<>());

        Mockito.when(routingService.getRouteByAddress(Mockito.any(RouteAddressRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/routing/address")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressRequest)))
                .andExpect(status().isOk());
                // .andExpect(jsonPath("$.fieldName").value("expectedValue")); // Adjust based on RouteResponse fields

        Mockito.verify(routingService).getRouteByAddress(Mockito.any(RouteAddressRequest.class));
    }

    @Test
    void testGetDirections_Success() throws Exception {
        RouteAddressRequest addressRequest = new RouteAddressRequest("OriginAddress", "DestinationAddress", "CAR");
        DirectionResponse mockResponse = new DirectionResponse(new ArrayList<>());

        Mockito.when(routingService.getDirections(Mockito.any(RouteAddressRequest.class))).thenReturn(mockResponse);

        mockMvc.perform(post("/api/routing/directions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(addressRequest)))
                .andExpect(status().isOk());
                //.andExpect(jsonPath("$.fieldName").value("expectedValue")); // Adjust based on DirectionResponse fields

        Mockito.verify(routingService).getDirections(Mockito.any(RouteAddressRequest.class));
    }

    @Test
    void testGetRouting_EmptyResponse() throws Exception {
        RouteRequest routeRequest = new RouteRequest(
                new Origin(new LatLng(new Location(10.0, 20.0))),
                new Destination(new LatLng(new Location(30.0, 40.0))),
                "CAR",
                "TRAFFIC_UNAWARE",
                false,
                new RouteModifiers(false, false, false),
                "en-US",
                "METRIC"
        );

        Mockito.when(routingService.getRoute(Mockito.any(RouteRequest.class))).thenReturn(null);

        mockMvc.perform(post("/api/routing/route")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(routeRequest)))
                .andExpect(status().isOk())
                .andExpect(content().string(""));
    }

}

