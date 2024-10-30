package fabiansig.routes;

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

    @Bean
    public RouterFunction<ServerResponse> chatGPTServiceRoute() {
        return GatewayRouterFunctions.route("chatGPT_service")
                .route(RequestPredicates.path(chatGPTServicePath), HandlerFunctions.http(chatGPTServiceIp))
                .build();
    }

    @Value("${springdoc.swagger-ui.urls[0].url}")
    private String chatGPTServiceSwaggerPath;

    @Bean
    public RouterFunction<ServerResponse> chatGPTServiceSwaggerRoute(){
        return route("chatGPT_service_swagger")
                .route(RequestPredicates.path(chatGPTServiceSwaggerPath), HandlerFunctions.http(chatGPTServiceIp))
                .filter(setPath("/api-docs"))
                .build();
    }

    @Value("${microservice.routing.path}")
    private String routingServicePath;
    @Value("${microservice.routing.ip}")
    private String routingServiceIp;

    @Bean
    public RouterFunction<ServerResponse> routingServiceRoute() {
        return GatewayRouterFunctions.route("routing_service")
                .route(RequestPredicates.path(routingServicePath), HandlerFunctions.http(routingServiceIp))
                .build();
    }

    @Value("${springdoc.swagger-ui.urls[1].url}")
    private String routingServiceSwaggerPath;

    @Bean
    public RouterFunction<ServerResponse> routingServiceSwaggerRoute(){
        return route("routing_service_swagger")
                .route(RequestPredicates.path(routingServiceSwaggerPath), HandlerFunctions.http(routingServiceIp))
                .filter(setPath("/api-docs")) //setPath rewrites the entire path of /aggregate/product-service/v3/api-docs to /api-docs
                .build();
    }


//    @Value("${microservice.weather.path}")
//    private String weatherServicePath;
//    @Value("${microservice.weather.ip}")
//    private String weatherServiceIp;
//
//    @Bean
//    public RouterFunction<ServerResponse> weatherServiceRoute() {
//        return GatewayRouterFunctions.route("weather_service")
//                .route(RequestPredicates.path(weatherServicePath), HandlerFunctions.http(weatherServiceIp))
//                .build();
//    }

    @Value("${microservice.speisekarte.path}")
    private String speisekarteServicePath;
    @Value("${microservice.speisekarte.ip}")
    private String speisekarteServiceIp;

    @Bean
    public RouterFunction<ServerResponse> speisekarteServiceRoute() {
        return GatewayRouterFunctions.route("speisekarte_service")
                .route(RequestPredicates.path(speisekarteServicePath), HandlerFunctions.http(speisekarteServiceIp))
                .build();
    }

    @Value("${springdoc.swagger-ui.urls[2].url}")
    private String speisekarteServiceSwaggerPath;

    @Bean
    public RouterFunction<ServerResponse> speisekarteServiceSwaggerRoute(){
        return route("speisekarte_service_swagger")
                .route(RequestPredicates.path(speisekarteServiceSwaggerPath), HandlerFunctions.http(speisekarteServiceIp))
                .filter(setPath("/api-docs")) //setPath rewrites the entire path of /aggregate/product-service/v3/api-docs to /api-docs
                .build();
    }

}
