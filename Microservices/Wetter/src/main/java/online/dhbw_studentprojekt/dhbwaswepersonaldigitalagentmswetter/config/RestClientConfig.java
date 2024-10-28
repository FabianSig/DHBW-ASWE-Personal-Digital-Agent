package online.dhbw_studentprojekt.dhbwaswepersonaldigitalagentmswetter.config;

import online.dhbw_studentprojekt.dhbwaswepersonaldigitalagentmswetter.client.WetterClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.Map;

@Configuration
public class RestClientConfig {
    @Value("${wetter.api.url}")
    private String wetterServiceUrl;
    private final String apiKey = System.getenv("API_KEY");
    private final Map<String, String> defaultUriVariables = Map.of("appID", apiKey);

    @Bean
    public WetterClient wetterClient() {

        RestClient restClient = RestClient.builder()
                .baseUrl(wetterServiceUrl)
                .defaultUriVariables(defaultUriVariables)
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(WetterClient.class);
    }

}
