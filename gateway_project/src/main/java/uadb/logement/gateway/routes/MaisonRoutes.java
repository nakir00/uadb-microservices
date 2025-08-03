package uadb.logement.gateway.routes;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.setPath;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;

@Configuration
public class MaisonRoutes {

    @Value("${logement.service.url}")
    private String logementServiceUrl;

    @Bean
    public RouterFunction<ServerResponse> locationServiceMaisonRouteNew() {
        return route("location_service_maison")
                .GET("/api/proprietaire/maison", http())
                    .before(uri(logementServiceUrl))
                    .before(setPath("/api/maison"))
                .POST("/api/proprietaire/maison", http())
                    .before(uri(logementServiceUrl))
                    .before(setPath("/api/maison"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> locationServiceMaisonRoutePut() {
        return route("location_service_maison_segment")
                .GET("/api/proprietaire/maison/{segment}", http())
                    .before(uri(logementServiceUrl))
                    .before(setPath("/api/maison/{segment}"))
                .PUT("/api/proprietaire/maison/{segment}", http())
                    .before(uri(logementServiceUrl))
                    .before(setPath("/api/maison/{segment}"))
                .DELETE("/api/proprietaire/maison/{segment}", http())
                    .before(uri(logementServiceUrl))
                    .before(setPath("/api/maison/{segment}"))
                .build();
    }

}
