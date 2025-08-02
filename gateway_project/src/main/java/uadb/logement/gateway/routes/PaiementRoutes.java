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
public class ContratRoutes {

    @Value("${contrat.service.url}")
    private String contratServiceUrl;

    @Bean
    public RouterFunction<ServerResponse> locationServiceContratRouteNew() {
        return route("location_service_chambre")
                .GET("/api/contrat", http())
                    .before(uri(contratServiceUrl))
                    .before(setPath("/api/contrat"))
                .POST("/api/contrat", http())
                    .before(uri(contratServiceUrl))
                    .before(setPath("/api/contrat"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> locationServiceContratRoutePut() {
        return route("location_service_contrat_segment")
                .GET("/api/contrat/{segment}", http())
                    .before(uri(contratServiceUrl))
                    .before(setPath("/api/contrat/{segment}"))
                .PUT("/api/contrat/{segment}", http())
                    .before(uri(contratServiceUrl))
                    .before(setPath("/api/contrat/{segment}"))
                .DELETE("/api/contrat/{segment}", http())
                    .before(uri(contratServiceUrl))
                    .before(setPath("/api/contrat/{segment}"))
                .build();
    }
}
