package online.dhbw_studentprojekt.msprefs.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@EnableWebSecurity
@Configuration
public class TestSecurityConfig {

    @Bean
    public SecurityFilterChain testSecurityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // Disable security for test profile

        return httpSecurity.authorizeHttpRequests(authorize -> authorize.anyRequest()
                        .permitAll())
                .csrf(AbstractHttpConfigurer::disable)
                .build();
    }
}

