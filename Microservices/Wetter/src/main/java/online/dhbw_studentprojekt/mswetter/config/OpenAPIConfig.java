package online.dhbw_studentprojekt.mswetter.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI wetterServiceAPI() {
        return new OpenAPI()
                .info(new Info().title("Wetter Service API")
                        .description("A microservice that calls the OpenWeatherMap API to get the weather forecast.")
                        .version("v0.0.1"));
    }

}
