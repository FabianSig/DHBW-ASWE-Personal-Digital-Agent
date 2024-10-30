package online.dhbw_studentprojekt.msprefs.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI prefsServiceApi() {
        return new OpenAPI()
                .info(new Info().title("Preferences Service API")
                        .description("Preferences Service API for managing products")
                        .version("v1.0.0")
                        .license(new License().name("MIT").url("https://opensource.org/licenses/MIT")));
    }

}
