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
public class ProblemeRoutes {

    @Value("${contrat.service.url}")
    private String contratServiceUrl;

    @Bean
    public RouterFunction<ServerResponse> locationServiceProblemeRouteNew() {
        return route("location_service_chambre")
                .GET("/api/probleme", http())
                    .before(uri(contratServiceUrl))
                    .before(setPath("/api/probleme"))
                .POST("/api/probleme", http())
                    .before(uri(contratServiceUrl))
                    .before(setPath("/api/probleme"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> locationServiceProblemeRoutePut() {
        return route("location_service_probleme_segment")
                .GET("/api/probleme/{segment}", http())
                    .before(uri(contratServiceUrl))
                    .before(setPath("/api/probleme/{segment}"))
                .PUT("/api/probleme/{segment}", http())
                    .before(uri(contratServiceUrl))
                    .before(setPath("/api/probleme/{segment}"))
                .DELETE("/api/probleme/{segment}", http())
                    .before(uri(contratServiceUrl))
                    .before(setPath("/api/probleme/{segment}"))
                .build();
    }
    @Bean
    public RouterFunction<ServerResponse> locationServiceProblemeRoutePutResolu() {
        return route("location_service_probleme_segment_resolu")
                .PUT("/api/probleme/{segment}/resolu", http())
                .before(uri(contratServiceUrl))
                .before(setPath("/api/probleme/{segment}/resolu"))
                .build();
    }
}
