package online.dhbw_studentprojekt.msmaps.service;

import online.dhbw_studentprojekt.dto.routing.custom.DirectionResponse;
import online.dhbw_studentprojekt.dto.routing.custom.RouteAddressRequest;
import online.dhbw_studentprojekt.dto.routing.geocoding.GeoCodingResponse;
import online.dhbw_studentprojekt.dto.routing.geocoding.Geometry;
import online.dhbw_studentprojekt.dto.routing.geocoding.Result;
import online.dhbw_studentprojekt.dto.routing.geocoding.Viewport;
import online.dhbw_studentprojekt.dto.routing.routing.*;
import online.dhbw_studentprojekt.msmaps.clients.MapsClient;
import online.dhbw_studentprojekt.msmaps.clients.RoutingClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Collections;

@ExtendWith(MockitoExtension.class)
class RoutingServiceTest {

    @Mock
    private RoutingClient routingClient;

    @Mock
    private MapsClient mapsClient;

    @Mock
    private GeoCodingService geoCodingService;

    @InjectMocks
    private RoutingService routingService;

    @Test
    void testGetRoute_Success() {
        // Mock input and output
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

        // Stubbing
        Mockito.when(routingClient.getRoute(routeRequest)).thenReturn(mockResponse);

        // Execute
        RouteResponse result = routingService.getRoute(routeRequest);

        // Verify
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockResponse, result);
        Mockito.verify(routingClient).getRoute(routeRequest);
    }

    @Test
    void testGetRouteByAddress_Success() {
        // Mock input and output
        RouteAddressRequest addressRequest = new RouteAddressRequest("OriginAddress", "DestinationAddress", "CAR");

        // Mock Geometry and Result
        Geometry mockGeometryOrigin = new Geometry(
                new online.dhbw_studentprojekt.dto.routing.geocoding.Location(10.0, 20.0),
                "ROOFTOP",
                new Viewport(new online.dhbw_studentprojekt.dto.routing.geocoding.Location(512.23, 4330.7),
                             new online.dhbw_studentprojekt.dto.routing.geocoding.Location(3246.77,33456))
        );
        Result mockResultOrigin = new Result(
                Collections.emptyList(),
                "OriginFormattedAddress",
                mockGeometryOrigin,
                "OriginPlaceId",
                null,
                Collections.singletonList("type1")
        );
        GeoCodingResponse mockOriginResponse = new GeoCodingResponse(Collections.singletonList(mockResultOrigin), "ok");

        Geometry mockGeometryDestination = new Geometry(
                new online.dhbw_studentprojekt.dto.routing.geocoding.Location(30.0, 40.0),
                "ROOFTOP",
                new Viewport((new online.dhbw_studentprojekt.dto.routing.geocoding.Location(512.23, 4330.7)),
                             new online.dhbw_studentprojekt.dto.routing.geocoding.Location(3246.77,33456))
        );
        Result mockResultDestination = new Result(
                Collections.emptyList(),
                "DestinationFormattedAddress",
                mockGeometryDestination,
                "DestinationPlaceId",
                null,
                Collections.singletonList("type2")
        );
        GeoCodingResponse mockDestinationResponse = new GeoCodingResponse(Collections.singletonList(mockResultDestination), "ok");

        // Mock RouteResponse
        RouteResponse mockRouteResponse = new RouteResponse(new ArrayList<>());

        // Stubbing
        Mockito.when(geoCodingService.getGeoCoding("OriginAddress")).thenReturn(mockOriginResponse);
        Mockito.when(geoCodingService.getGeoCoding("DestinationAddress")).thenReturn(mockDestinationResponse);
        Mockito.when(routingClient.getRoute(Mockito.any(RouteRequest.class))).thenReturn(mockRouteResponse);

        // Execute
        RouteResponse result = routingService.getRouteByAddress(addressRequest);

        // Verify
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockRouteResponse, result);

        Mockito.verify(geoCodingService).getGeoCoding("OriginAddress");
        Mockito.verify(geoCodingService).getGeoCoding("DestinationAddress");
        Mockito.verify(routingClient).getRoute(Mockito.any(RouteRequest.class));
    }
    @Test
    void testGetDirections_Success() {
        // Mock input
        RouteAddressRequest addressRequest = new RouteAddressRequest("OriginAddress", "DestinationAddress", "CAR");

        // Mock Geometry and Result
        Geometry mockGeometryOrigin = new Geometry(
                new online.dhbw_studentprojekt.dto.routing.geocoding.Location(10.0, 20.0),
                "ROOFTOP",
                new Viewport((new online.dhbw_studentprojekt.dto.routing.geocoding.Location(512.23, 4330.7)),
                             new online.dhbw_studentprojekt.dto.routing.geocoding.Location(3246.77,33456))
        );
        Result mockResultOrigin = new Result(
                Collections.emptyList(),
                "OriginFormattedAddress",
                mockGeometryOrigin,
                "OriginPlaceId",
                null,
                Collections.singletonList("type1")
        );
        GeoCodingResponse mockOriginResponse = new GeoCodingResponse(Collections.singletonList(mockResultOrigin), "ok");

        Geometry mockGeometryDestination = new Geometry(
                new online.dhbw_studentprojekt.dto.routing.geocoding.Location(30.0, 40.0),
                "ROOFTOP",
                new Viewport((new online.dhbw_studentprojekt.dto.routing.geocoding.Location(512.23, 4330.7)),
                            new online.dhbw_studentprojekt.dto.routing.geocoding.Location(3246.77,33456))
        );
        Result mockResultDestination = new Result(
                Collections.emptyList(),
                "DestinationFormattedAddress",
                mockGeometryDestination,
                "DestinationPlaceId",
                null,
                Collections.singletonList("type2")
        );
        GeoCodingResponse mockDestinationResponse = new GeoCodingResponse(Collections.singletonList(mockResultDestination), "ok");

        // Mock DirectionResponse
        DirectionResponse mockDirectionResponse = new DirectionResponse(new ArrayList<>());

        // Stubbing
        Mockito.when(geoCodingService.getGeoCoding("OriginAddress")).thenReturn(mockOriginResponse);
        Mockito.when(geoCodingService.getGeoCoding("DestinationAddress")).thenReturn(mockDestinationResponse);
        Mockito.when(mapsClient.getDirections("place_id:OriginPlaceId", "place_id:DestinationPlaceId", System.getenv("API_KEY"), "CAR"))
                .thenReturn(mockDirectionResponse);

        // Execute
        DirectionResponse result = routingService.getDirections(addressRequest);

        // Verify
        Assertions.assertNotNull(result);
        Assertions.assertEquals(mockDirectionResponse, result);

        Mockito.verify(geoCodingService).getGeoCoding("OriginAddress");
        Mockito.verify(geoCodingService).getGeoCoding("DestinationAddress");
        Mockito.verify(mapsClient).getDirections("place_id:OriginPlaceId", "place_id:DestinationPlaceId", System.getenv("API_KEY"), "CAR");
    }


}
