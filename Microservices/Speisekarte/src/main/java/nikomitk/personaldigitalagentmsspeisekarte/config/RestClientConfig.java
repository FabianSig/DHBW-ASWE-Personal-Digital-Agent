package nikomitk.personaldigitalagentmsspeisekarte.config;

import nikomitk.personaldigitalagentmsspeisekarte.client.SpeisekarteClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.util.function.Consumer;

@Configuration
public class RestClientConfig {

    @Value("${speisekarte.api.url}")
    private String speisekarteServiceUrl;

    @Bean
    public SpeisekarteClient speisekarteClient() {


        RestClient restClient = RestClient.builder()
                .baseUrl(speisekarteServiceUrl)
                .defaultHeaders(httpHeaders -> {
                    httpHeaders.addAll(myHeaders());
                })
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(SpeisekarteClient.class);
    }

    private static HttpHeaders myHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.set("User-Agent", "Mozilla/5.0 (X11; Linux x86_64; rv:131.0) Gecko/20100101 Firefox/131.0");
        headers.set("Accept", "*/*");
        headers.set("Accept-Language", "de,en-US;q=0.7,en;q=0.3");
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.set("X-Requested-With", "XMLHttpRequest");
        headers.set("Sec-GPC", "1");
        headers.set("Sec-Fetch-Dest", "empty");
        headers.set("Sec-Fetch-Mode", "cors");
        headers.set("Sec-Fetch-Site", "same-origin");
        headers.set("Priority", "u=0");
        headers.setCacheControl("no-cache");

        return headers;
    }

}
