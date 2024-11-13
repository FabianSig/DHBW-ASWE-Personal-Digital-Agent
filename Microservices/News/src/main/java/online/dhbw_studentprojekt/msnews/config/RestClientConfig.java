package online.dhbw_studentprojekt.msnews.config;

import lombok.extern.slf4j.Slf4j;
import online.dhbw_studentprojekt.msnews.client.NewsClient;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.lang.NonNull;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;


@Slf4j
@Configuration
public class RestClientConfig {

    @Value("${news.api.url}")
    private String newsServiceUrl;

    @Bean
    public NewsClient newsClient() {

        RestClient restClient = RestClient.builder()
                .baseUrl(newsServiceUrl)
                .requestInterceptor(new ApiKeyInterceptor())
                .build();


        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();
        return httpServiceProxyFactory.createClient(NewsClient.class);
    }

    /**
     * Since the api uses a query parameter for the api key, the default way of using api keys (auth header) can't be used
     */
    private static class ApiKeyInterceptor implements ClientHttpRequestInterceptor {

        @Override
        @NonNull
        public ClientHttpResponse intercept(HttpRequest request, @NonNull byte[] body, ClientHttpRequestExecution execution) throws IOException {

            try {
                URI uri = new URI(request.getURI() + "&apiKey=" + System.getenv("API_KEY"));
                HttpRequest modifiedRequest = new HttpRequestWrapper(request) {
                    @Override
                    @NonNull
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
