package fabiansig.config;

import fabiansig.clients.GeoCodingClient;
import fabiansig.clients.RoutingClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@Slf4j
public class RestClientConfig {

    @Value("${routes.google.api.url}")
    private String routesGoogleServiceUrl;

    @Value("${maps.google.api.url}")
    private String mapsGoogleServiceUrl;

    @Bean
    public RoutingClient routingClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(routesGoogleServiceUrl)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .defaultHeader("X-Goog-Api-Key", System.getenv("API_KEY"))
                .defaultHeader("X-Goog-FieldMask", "routes.duration,routes.distanceMeters,routes.polyline.encodedPolyline")
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(RoutingClient.class);
    }

    @Bean
    public GeoCodingClient geoCodingClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(mapsGoogleServiceUrl)
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(GeoCodingClient.class);
    }
}
