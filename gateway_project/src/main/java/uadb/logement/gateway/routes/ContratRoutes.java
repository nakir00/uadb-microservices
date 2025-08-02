package uadb.logement.gateway.routes;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerRequest;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.setPath;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;

@Configuration
public class ChambreRoutes {

    @Value("${logement.service.url}")
    private String logementServiceUrl;



    @Bean
    public RouterFunction<ServerResponse> locationServiceChambreRouteNew() {
        return route("location_service_chambre")
                .GET("/api/proprietaire/chambre", http())
                    .before(uri(logementServiceUrl))
                    .before(setPath("/api/chambre"))
                .POST("/api/proprietaire/chambre", http())
                    .before(uri(logementServiceUrl))
                    .before(setPath("/api/chambre"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> locationServiceChambreRouteGuestNew() {
        return route("location_service_chambre")
                .GET("/api/guest/chambre", http())
                .before(uri(logementServiceUrl))
                .before(setPath("/api/chambre/visiteur"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> locationServiceChambreRoutePut() {
        return route("location_service_chambre_segment")
                .GET("/api/proprietaire/chambre/{segment}", http())
                    .before(uri(logementServiceUrl))
                    .before(setPath("/api/chambre/{segment}"))
                .PUT("/api/proprietaire/chambre/{segment}", http())
                    .before(uri(logementServiceUrl))
                    .before(setPath("/api/chambre/{segment}"))
                .DELETE("/api/proprietaire/chambre/{segment}", http())
                    .before(uri(logementServiceUrl))
                    .before(setPath("/api/chambre/{segment}"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> locationServiceChambreRouteGuestPut() {
        return route("location_service_chambre_segment")
                .GET("/api/guest/chambre/{segment}", http())
                .before(uri(logementServiceUrl))
                .before(setPath("/api/chambre/{segment}"))
                .PUT("/api/guest/chambre/{segment}", http())
                .before(uri(logementServiceUrl))
                .before(setPath("/api/chambre/{segment}"))
                .DELETE("/api/guest/chambre/{segment}", http())
                .before(uri(logementServiceUrl))
                .before(setPath("/api/chambre/{segment}"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> locationServiceChambreRouteMedia() {
        return route("location_service_chambre_segment_media")
                .GET("/api/proprietaire/chambre/{segment}/media", http())
                    .before(uri(logementServiceUrl))
                    .before(setPath("/api/chambre/{segment}/media"))
                .POST("/api/proprietaire/chambre/{segment}/media", http())
                    .before(uri(logementServiceUrl))
                    .before(setPath("/api/chambre/{segment}/media"))
                .PUT("/api/proprietaire/chambre/{segment}/media", http())
                    .before(uri(logementServiceUrl))
                    .before(setPath("/api/chambre/{segment}/media"))
                .DELETE("/api/proprietaire/chambre/{segment}/media", http())
                    .before(uri(logementServiceUrl))
                    .before(setPath("/api/chambre/{segment}/media"))
                .build();
    }

    @Bean
    public RouterFunction<ServerResponse> locationServiceChambreRouteMediaPut() {
        return route("location_service_chambre_segment_media")
                .GET("/api/proprietaire/chambre/{segment}/media/{mediaId}", http())
                    .before(uri(logementServiceUrl))
                    .before(setPath("/api/chambre/{segment}/media/{mediaId}"))
                .PUT("/api/proprietaire/chambre/{segment}/media/{mediaId}", http())
                    .before(uri(logementServiceUrl))
                    .before(setPath("/api/chambre/{segment}/media/{mediaId}"))
                .DELETE("/api/proprietaire/chambre/{segment}/media/{mediaId}", http())
                    .before(uri(logementServiceUrl))
                    .before(setPath("/api/chambre/{segment}/media/{mediaId}"))
                .build();
    }
}
