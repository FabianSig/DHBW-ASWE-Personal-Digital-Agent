package sven.news.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import sven.news.client.NewsClient;


    
    @Configuration
    public class RestClientConfig{
        @Value("${news.api.url}")
        private String newsServiceUrl;

        @Bean
        public NewsClient newsClient() {

            RestClient restClient =RestClient.builder()
                .baseUrl(newsServiceUrl)
                .build();

        
            RestClientAdapter restClinetAdapter = RestClientAdapter.create(restClient);
            HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClinetAdapter).build();
            return httpServiceProxyFactory.createClient(NewsClient.class);
        }
}
