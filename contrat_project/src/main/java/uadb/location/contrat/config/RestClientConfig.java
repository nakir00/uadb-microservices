package uadb.location.contrat.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;
import uadb.location.contrat.client.CDNClient;
import uadb.location.contrat.client.UtilisateurClient;
//import uadb.location.logement.client.UtilisateurClient;


@Configuration
public class RestClientConfig {

    @Value("${gateway.service.url}")
    private String gatewayServiceUrl;

    @Value("${cdn.service.url}")
    private String cdnServiceUrl;


    @Bean
    public UtilisateurClient utilisateurClient() {

        RestClient restClient = RestClient.builder().baseUrl(gatewayServiceUrl).build();
        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();

        return httpServiceProxyFactory.createClient(UtilisateurClient.class);
    }

    @Bean
    public CDNClient mediaClient() {

        RestClient restClient = RestClient.builder().baseUrl(cdnServiceUrl).build();
        RestClientAdapter restClientAdapter = RestClientAdapter.create(restClient);
        HttpServiceProxyFactory httpServiceProxyFactory = HttpServiceProxyFactory.builderFor(restClientAdapter).build();

        return httpServiceProxyFactory.createClient(CDNClient.class);
    }
}