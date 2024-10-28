package sven.news.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;


@Configuration
public class OpenAPIConfig {


    @Bean
    public OpenAPI newsServiceAPI(){
            return new OpenAPI()
                .info(new Info().title("News Service API")
                .description("A microservice that calls an News API to get News Articles for Spesific Preferences")
                .version("v0.0.1"));
    }
}
