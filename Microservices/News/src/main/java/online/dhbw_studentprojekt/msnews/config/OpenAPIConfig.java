package online.dhbw_studentprojekt.msnews.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenAPIConfig {


    @Bean
    public OpenAPI newsServiceAPI() {

        return new OpenAPI()
                .info(new Info().title("News Service API")
                        .description("A microservice that calls an News API to get News Articles for Spesific Preferences")
                        .version("v0.0.1"));
    }

}
