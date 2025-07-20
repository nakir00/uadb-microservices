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
    public RouterFunction<ServerResponse> locationServiceSwaggerRoute() {
        return route("location_service_swagger")
                .GET("/aggregate/logement-service/v3/api-docs", http())
                .before(uri(logementServiceUrl))
                .before(setPath("/api-docs"))
                .build();
    }


    /*@Bean
    public RouterFunction<ServerResponse> locationServiceMaisonRoute() {
        System.out.println(logementServiceUrl+ "/api/maison");
        return GatewayRouterFunctions.route("location_service_maison")
                .route(RequestPredicates.GET("/api/proprietaire/maison"), HandlerFunctions.http(logementServiceUrl))
                .route(RequestPredicates.POST("/api/proprietaire/maison"), HandlerFunctions.http(logementServiceUrl+"/api/maison"))
                .route(RequestPredicates.PUT("/api/proprietaire/maison/{maisonId}"),
                        request -> forwardWithPathVariable(request, "maisonId", logementServiceUrl+"/api/maison"))
                .route(RequestPredicates.DELETE("/api/proprietaire/maison/{maisonId}"),
                        request -> forwardWithPathVariable(request, "maisonId", logementServiceUrl+"/api/maison"))
                .build();
    }*/

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

    private static ServerResponse forwardWithPathVariable(ServerRequest request, String pathVariable, String baseURl) throws Exception {
        String value = request.pathVariable(pathVariable);
        return HandlerFunctions.http(baseURl + value).handle(request);
    }

}
