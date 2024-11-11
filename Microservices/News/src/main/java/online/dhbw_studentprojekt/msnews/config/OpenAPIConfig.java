package online.dhbw_studentprojekt.msnews.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class OpenAPIConfig {


    @Bean
    public OpenAPI newsServiceAPI() {

        return new OpenAPI()
                .info(new Info().title("News Service API")
                        .description("A microservice that calls an News API to get News Articles for Specific Preferences")
                        .version("v0.0.1"))
                .addServersItem(new Server().url("http://localhost:8082"));
    }

}
