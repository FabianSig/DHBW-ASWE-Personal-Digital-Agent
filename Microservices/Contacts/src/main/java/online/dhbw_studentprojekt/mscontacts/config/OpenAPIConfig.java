package online.dhbw_studentprojekt.mscontacts.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI contactsServiceAPI() {
        return new OpenAPI()
                .info(new Info().title("Contacts Service API")
                        .description("A microservice to fetch unread emails and the dates of the last call with certain contacts.")
                        .version("v0.0.1"))
                .addServersItem(new Server().url("http://localhost:8089"));
    }

}
