package com.example.dhbwaswepersonaldigitalagentmswetter.config;

import com.example.dhbwaswepersonaldigitalagentmswetter.client.WetterClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class RestClientConfig {
    @Value("${wetter.api.url}")
    private String wetterServiceUrl;

    @Bean
    public WetterClient wetterClient() {

        RestClient restClient = RestClient.builder()
                .baseUrl(wetterServiceUrl)
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(WetterClient.class);
    }

}
