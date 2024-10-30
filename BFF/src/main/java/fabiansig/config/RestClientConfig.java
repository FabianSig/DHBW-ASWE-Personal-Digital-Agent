package fabiansig.config;

import fabiansig.client.ChatGPTClient;
import fabiansig.client.MapsClient;
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


    @Value("${microservice.chatgpt.ip}")
    private String chatGPTServiceUrl;

    @Value("${microservice.routing.ip}")
    private String mapsServiceUrl;


    @Bean
    public ChatGPTClient chatGPTClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(chatGPTServiceUrl)
                .defaultHeader("Authorization", System.getenv("API_KEY"))
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(ChatGPTClient.class);
    }

    @Bean
    public MapsClient mapsClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(mapsServiceUrl)
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(MapsClient.class);
    }
}
