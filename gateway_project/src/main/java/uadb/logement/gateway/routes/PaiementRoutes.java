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
public class PaiementRoutes {

    @Value("${contrat.service.url}")
    private String contratServiceUrl;

    @Bean
    public RouterFunction<ServerResponse> locationServicePaiementRouteNew() {
        return route("location_service_chambre")
                .GET("/api/paiement", http())
                    .before(uri(contratServiceUrl))
                    .before(setPath("/api/paiement"))

                .POST("/api/paiement", http())
                    .before(uri(contratServiceUrl))
                    .before(setPath("/api/paiement"))
                .build();
    }
    @Bean
    public RouterFunction<ServerResponse> locationServicePaiementRouteGetAll() {
        return route("contrat_service_paiement")
                .GET("/api/paiement/all", http())
                    .before(uri(contratServiceUrl))
                    .before(setPath("/api/paiement/all"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> locationServicePaiementRoutePut() {
        return route("location_service_paiement_segment")
                .GET("/api/paiement/{segment}", http())
                    .before(uri(contratServiceUrl))
                    .before(setPath("/api/paiement/{segment}"))
                .PUT("/api/paiement/{segment}", http())
                    .before(uri(contratServiceUrl))
                    .before(setPath("/api/paiement/{segment}"))
                .DELETE("/api/paiement/{segment}", http())
                    .before(uri(contratServiceUrl))
                    .before(setPath("/api/paiement/{segment}"))
                .build();
    }
}
