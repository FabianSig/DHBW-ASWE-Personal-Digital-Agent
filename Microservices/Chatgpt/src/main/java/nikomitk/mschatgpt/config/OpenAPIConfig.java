package nikomitk.mschatgpt.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenAPIConfig {

    @Bean
    public OpenAPI chatGPTServiceAPI() {
        return new OpenAPI()
                .info(new Info().title("ChatGPT Service API")
                        .description("A microservice to interact with a GPT model for chatbot responses.")
                        .version("v0.0.1"));
    }

}
