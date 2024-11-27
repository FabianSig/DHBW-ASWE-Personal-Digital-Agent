package online.dhbw_studentprojekt.mschatgpt.config;

import lombok.extern.slf4j.Slf4j;
import online.dhbw_studentprojekt.mschatgpt.client.ChatGPTClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@Slf4j
public class RestClientConfig {

    @Value("${chatgpt.api.url}")
    private String chatGPTServiceUrl;

    @Bean
    public ChatGPTClient chatGPTClient() {

        RestClient restClient = RestClient.builder()
                .baseUrl(chatGPTServiceUrl)
                .defaultHeader("Authorization", "Bearer " + System.getenv("API_KEY"))
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(ChatGPTClient.class);
    }

}