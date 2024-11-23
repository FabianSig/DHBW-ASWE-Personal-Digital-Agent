package online.dhbw_studentprojekt.mscontacts.config;

import online.dhbw_studentprojekt.mscontacts.client.EmailClient;
import online.dhbw_studentprojekt.mscontacts.client.PhoneClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {

    @Value("${email.api.url}")
    private String emailApiUrl;

    @Bean
    public EmailClient chatGPTClient() {

        RestClient restClient = RestClient.builder()
                .baseUrl(emailApiUrl)
                .defaultHeader("Authorization", "Bearer " + System.getenv("API_KEY"))
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(EmailClient.class);
    }

    @Value("${phone.api.url}")
    private String phoneApiUrl;

    @Bean
    public PhoneClient phoneClient() {

        RestClient restClient = RestClient.builder()
                .baseUrl(phoneApiUrl)
                .defaultHeader("Authorization", "Bearer " + System.getenv("API_KEY"))
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(PhoneClient.class);
    }

}
