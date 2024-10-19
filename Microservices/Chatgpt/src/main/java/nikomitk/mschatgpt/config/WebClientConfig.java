package nikomitk.mschatgpt.config;

import lombok.extern.slf4j.Slf4j;
import nikomitk.mschatgpt.ChatGPTClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.support.WebClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
@Slf4j
public class WebClientConfig {

    @Value("${chatgpt.api.url}")
    private String chatGPTServiceUrl;

    @Bean
    public ChatGPTClient chatGPTClient() {
        WebClient webClient = WebClient.builder()
                .baseUrl(chatGPTServiceUrl)
                .defaultHeader("Authorization", "Bearer " + System.getenv("API_KEY"))
                .build();

        WebClientAdapter webClientAdapter = WebClientAdapter.create(webClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(webClientAdapter).build();
        return httpServiceProxyFactory.createClient(ChatGPTClient.class);
    }
}
