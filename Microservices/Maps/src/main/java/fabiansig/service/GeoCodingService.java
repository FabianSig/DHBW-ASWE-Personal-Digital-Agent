package fabiansig.service;

import fabiansig.clients.GeoCodingClient;
import fabiansig.dto.geocoding.GeoCodingResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class GeoCodingService {

    private final GeoCodingClient geoCodingClient;
    private final String API_KEY = System.getenv("API_KEY");

    public GeoCodingResponse getGeoCoding(String address) {
        return geoCodingClient.getGeoCoding(address.replace(" ", "+"), API_KEY);
    }
}
