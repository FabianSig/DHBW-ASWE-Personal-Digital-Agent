package online.dhbw_studentprojekt.bff.config;

import lombok.extern.slf4j.Slf4j;
import online.dhbw_studentprojekt.bff.client.*;
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

    @Value("${microservice.speisekarte.ip}")
    private String speisekarteServiceUrl;

    @Value("${microservice.rapla.ip}")
    private String raplaServiceUrl;

    @Value("${microservice.prefs.ip}")
    private String prefsServiceUrl;

    @Value("${microservice.stock.ip}")
    private String stockServiceUrl;

    @Value("${microservice.news.ip}")
    private String newsServiceUrl;

    @Bean
    public ChatGPTClient chatGPTClient() {

        RestClient restClient = RestClient.builder()
                .baseUrl(chatGPTServiceUrl)
                .defaultHeader("Authorization", System.getenv("OUR_API_KEY"))
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(ChatGPTClient.class);
    }

    @Bean
    public MapsClient mapsClient() {

        RestClient restClient = RestClient.builder()
                .baseUrl(mapsServiceUrl)
                .defaultHeader("Authorization", System.getenv("OUR_API_KEY"))
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(MapsClient.class);
    }

    @Bean
    public SpeisekarteClient speisekarteClient() {

        RestClient restClient = RestClient.builder()
                .baseUrl(speisekarteServiceUrl)
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(SpeisekarteClient.class);
    }

    @Bean
    public RaplaClient raplaClient() {

        RestClient restClient = RestClient.builder()
                .baseUrl(raplaServiceUrl)
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(RaplaClient.class);
    }

    @Bean
    public PrefsClient prefsClient() {

        RestClient restClient = RestClient.builder()
                .baseUrl(prefsServiceUrl)
                .defaultHeader("Authorization", System.getenv("OUR_API_KEY"))
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(PrefsClient.class);
    }

    @Bean
    public StockClient stockClient() {

        RestClient restClient = RestClient.builder()
                .baseUrl(stockServiceUrl)
                .defaultHeader("Authorization", System.getenv("OUR_API_KEY"))
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(StockClient.class);
    }

    @Bean
    public NewsClient newsClient() {

        RestClient restClient = RestClient.builder()
                .baseUrl(newsServiceUrl)
                .defaultHeader("Authorization", System.getenv("OUR_API_KEY"))
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(NewsClient.class);
    }

}
