package nikomitk.personaldigitalagentmsspeisekarte.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI speisekarteServiceAPI() {
        return new OpenAPI()
                .info(new Info().title("Speise Service API")
                        .description("A microservice that calls the api from the Studierendenwerk stuttgart to get the current menu at mensa central. Because this api is undocumented i had to reverse engineer the api calls.")
                        .version("v0.0.1"));
    }

}
