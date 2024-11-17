package online.dhbw_studentprojekt.mswetter.config;

import online.dhbw_studentprojekt.mswetter.client.WetterClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
public class RestClientConfig {

    @Value("${wetter.api.url}")
    private String wetterServiceUrl;

    @Bean
    public WetterClient wetterClient() {

        RestClient restClient = RestClient.builder()
                .baseUrl(wetterServiceUrl)
                .requestInterceptor(new ApiKeyInterceptor())
                .build();

        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(WetterClient.class);
    }

    private static class ApiKeyInterceptor implements ClientHttpRequestInterceptor {

        @Override
        public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution) throws IOException {

            try {
                URI uri = new URI(request.getURI() + "&appid=" + System.getenv("API_KEY"));
                HttpRequest modifiedRequest = new HttpRequestWrapper(request) {
                    @Override
                    public URI getURI() {

                        return uri;
                    }
                };
                return execution.execute(modifiedRequest, body);
            } catch (URISyntaxException e) {
                throw new IOException("Failed to add API key to request", e);
            }
        }

    }

}
