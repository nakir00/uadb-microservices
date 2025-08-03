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
public class RendezVousRoute {

    @Value("${logement.service.url}")
    private String logementServiceUrl;

    @Bean
    public RouterFunction<ServerResponse> locationServiceRendezVousRouteNew() {
        return route("location_service_rendez_vous")
                .GET("/api/rendez-vous", http())
                    .before(uri(logementServiceUrl))
                    .before(setPath("/api/rendez-vous"))
                .GET("/api/rendez-vous/proprietaire", http())
                    .before(uri(logementServiceUrl))
                    .before(setPath("/api/rendez-vous"))
                .POST("/api/rendez-vous", http())
                    .before(uri(logementServiceUrl))
                    .before(setPath("/api/rendez-vous"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> locationServiceRendezVousRoutePut() {
        return route("location_service_rendez_vous_segment")
                .GET("/api/rendez-vous/{segment}", http())
                .before(uri(logementServiceUrl))
                .before(setPath("/api/rendez-vous/{segment}"))
                .PUT("/api/rendez-vous/{segment}", http())
                .before(uri(logementServiceUrl))
                .before(setPath("/api/rendez-vous/{segment}"))
                .DELETE("/api/rendez-vous/{segment}", http())
                .before(uri(logementServiceUrl))
                .before(setPath("/api/rendez-vous/{segment}"))
                .build();
    }

}
