package online.dhbw_studentprojekt.bff.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RequestPredicates;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.FilterFunctions.setPath;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;

@Configuration
public class Routes {

    @Value("${microservice.chatgpt.path}")
    private String chatGPTServicePath;
    @Value("${microservice.chatgpt.ip}")
    private String chatGPTServiceIp;
    @Value("${springdoc.swagger-ui.urls[0].url}")
    private String chatGPTServiceSwaggerPath;
    @Value("${microservice.routing.path}")
    private String routingServicePath;
    @Value("${microservice.routing.ip}")
    private String routingServiceIp;
    @Value("${microservice.geocoding.path}")
    private String geocodingServicePath;
    @Value("${springdoc.swagger-ui.urls[1].url}")
    private String routingServiceSwaggerPath;
    @Value("${microservice.weather.path}")
    private String weatherServicePath;
    @Value("${microservice.weather.ip}")
    private String weatherServiceIp;
    @Value("${microservice.speisekarte.path}")
    private String speisekarteServicePath;
    @Value("${microservice.speisekarte.ip}")
    private String speisekarteServiceIp;
    @Value("${springdoc.swagger-ui.urls[2].url}")
    private String speisekarteServiceSwaggerPath;
    @Value("${microservice.prefs.path}")
    private String prefsServicePath;
    @Value("${microservice.prefs.ip}")
    private String prefsServiceIp;
    @Value("${springdoc.swagger-ui.urls[3].url}")
    private String prefsServiceSwaggerPath;
    @Value("${microservice.stock.path}")
    private String stockServicePath;
    @Value("${microservice.stock.ip}")
    private String stockServiceIp;
    @Value("${springdoc.swagger-ui.urls[4].url}")
    private String stockServiceSwaggerPath;
    @Value("${microservice.news.ip}")
    private String newsServiceIp;
    @Value("${microservice.news.path}")
    private String newsServicePath;
    @Value("${springdoc.swagger-ui.urls[5].url}")
    private String newsServiceSwaggerPath;
    @Value("${microservice.contacts.ip}")
    private String contactsServiceIp;
    @Value("${microservice.contacts.path}")
    private String contactsServicePath;
    @Value("${springdoc.swagger-ui.urls[6].url}")
    private String contactsServiceSwaggerPath;

    @Bean
    public RouterFunction<ServerResponse> chatGPTServiceRoute() {

        return GatewayRouterFunctions.route("chatGPT_service")
                .route(RequestPredicates.path(chatGPTServicePath), HandlerFunctions.http(chatGPTServiceIp))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> chatGPTServiceSwaggerRoute() {

        return route("chatGPT_service_swagger")
                .route(RequestPredicates.path(chatGPTServiceSwaggerPath), HandlerFunctions.http(chatGPTServiceIp))
                .filter(setPath("/api-docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> routingServiceRoute() {

        return GatewayRouterFunctions.route("routing_service")
                .route(RequestPredicates.path(routingServicePath), HandlerFunctions.http(routingServiceIp))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> geocodingServiceRoute() {

        return GatewayRouterFunctions.route("geocoding_service")
                .route(RequestPredicates.path(geocodingServicePath), HandlerFunctions.http(routingServiceIp))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> routingServiceSwaggerRoute() {

        return route("routing_service_swagger")
                .route(RequestPredicates.path(routingServiceSwaggerPath), HandlerFunctions.http(routingServiceIp))
                .filter(setPath("/api-docs")) //setPath rewrites the entire path of /aggregate/product-service/v3/api-docs to /api-docs
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> weatherServiceRoute() {

        return GatewayRouterFunctions.route("weather_service")
                .route(RequestPredicates.path(weatherServicePath), HandlerFunctions.http(weatherServiceIp))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> speisekarteServiceRoute() {

        return GatewayRouterFunctions.route("speisekarte_service")
                .route(RequestPredicates.path(speisekarteServicePath), HandlerFunctions.http(speisekarteServiceIp))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> speisekarteServiceSwaggerRoute() {

        return route("speisekarte_service_swagger")
                .route(RequestPredicates.path(speisekarteServiceSwaggerPath), HandlerFunctions.http(speisekarteServiceIp))
                .filter(setPath("/api-docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> prefsServiceRoute() {

        return GatewayRouterFunctions.route("prefs_service")
                .route(RequestPredicates.path(prefsServicePath), HandlerFunctions.http(prefsServiceIp))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> prefsServiceSwaggerRoute() {

        return route("prefs_service_swagger")
                .route(RequestPredicates.path(prefsServiceSwaggerPath), HandlerFunctions.http(prefsServiceIp))
                .filter(setPath("/api-docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> stockServiceRoute() {

        return GatewayRouterFunctions.route("stock_service")
                .route(RequestPredicates.path(stockServicePath), HandlerFunctions.http(stockServiceIp))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> stockServiceSwaggerRoute() {

        return route("stock_service_swagger")
                .route(RequestPredicates.path(stockServiceSwaggerPath), HandlerFunctions.http(stockServiceIp))
                .filter(setPath("/api-docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> newsServiceRoute() {

        return route("news_service")
                .route(RequestPredicates.path(newsServicePath), HandlerFunctions.http(newsServiceIp))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> newsServiceSwaggerRoute() {

        return route("news_service_swagger")
                .route(RequestPredicates.path(newsServiceSwaggerPath), HandlerFunctions.http(newsServiceIp))
                .filter(setPath("/api-docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> contactsServiceRoute() {

        return route("contacts_service")
                .route(RequestPredicates.path(contactsServicePath), HandlerFunctions.http(contactsServiceIp))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> contactsServiceSwaggerRoute() {

        return route("contacts_service_swagger")
                .route(RequestPredicates.path(contactsServiceSwaggerPath), HandlerFunctions.http(contactsServiceIp))
                .filter(setPath("/api-docs"))
                .build();
    }

}
