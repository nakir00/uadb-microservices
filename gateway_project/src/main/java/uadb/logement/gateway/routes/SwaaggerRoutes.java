package uadb.logement.gateway.routes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.setPath;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

@Configuration
public class SwaaggerRoutes {

    @Value("${logement.service.url}")
    private String logementServiceUrl;

    @Value("${cdn.service.url}")
    private String cdnServiceUrl;

    @Value("${contrat.service.url}")
    private String contratServiceUrl;

    @Bean
    public RouterFunction<ServerResponse> locationServiceSwaggerRoute() {
        return route("location_service_swagger")
                .GET("/aggregate/logement-service/v3/api-docs", http())
                .before(uri(logementServiceUrl))
                .before(setPath("/api-docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> cdnServiceSwaggerRoute() {
        return route("cdn_service_swagger")
                .GET("/aggregate/cdn-service/v3/api-docs", http())
                .before(uri(cdnServiceUrl))
                .before(setPath("/api-docs"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> contratServiceSwaggerRoute() {
        return route("contrat_service_swagger")
                .GET("/aggregate/contrat-service/v3/api-docs", http())
                .before(uri(contratServiceUrl))
                .before(setPath("/api-docs"))
                .build();
    }
}
