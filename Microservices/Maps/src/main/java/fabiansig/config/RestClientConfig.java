package fabiansig.config;

import fabiansig.GoogleClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@Slf4j
public class RestClientConfig {

    @Value("${google.api.url}")
    private String googleServiceUrl;

    @Bean
    public GoogleClient googleClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(googleServiceUrl)
                .defaultHeader("X-Goog-Api-Key", "Bearer " + System.getenv("API_KEY"))
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(GoogleClient.class);
    }
}
