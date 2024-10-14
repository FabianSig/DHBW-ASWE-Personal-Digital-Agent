package nikomitk.mschatgpt.config;

import nikomitk.mschatgpt.ChatGPTClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Value("${chatgpt.api.url}")
    private String chatGPTServiceUrl;

    @Bean
    public ChatGPTClient chatGPTClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl(chatGPTServiceUrl)
                .build();
        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(ChatGPTClient.class);
    }
}
