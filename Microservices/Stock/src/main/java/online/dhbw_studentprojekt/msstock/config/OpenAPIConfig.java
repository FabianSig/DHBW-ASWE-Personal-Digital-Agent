package online.dhbw_studentprojekt.msstock.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI stockServiceAPI() {

        return new OpenAPI()
                .info(new Info().title("Stock Service API")
                        .description("A microservice that calls the alpha vantage api.")
                        .version("v0.0.1"))
                .addServersItem(new Server().url("http://localhost:8083"));
    }

}
