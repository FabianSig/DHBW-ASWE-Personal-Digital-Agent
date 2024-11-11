package online.dhbw_studentprojekt.msmaps.service;

import online.dhbw_studentprojekt.msmaps.clients.MapsClient;
import online.dhbw_studentprojekt.dto.routing.geocoding.GeoCodingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeoCodingService {

    private final MapsClient mapsClient;
    private final String API_KEY = System.getenv("API_KEY");

    public GeoCodingResponse getGeoCoding(String address) {
        return mapsClient.getGeoCoding(address.replace(" ", "+"), API_KEY);
    }
}
