package pl.joboffers.http.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import pl.joboffers.domain.offer.OfferFetchable;
import pl.joboffers.infrastructure.offer.http.OfferFetcherClientConfig;
import pl.joboffers.infrastructure.offer.http.OfferFetcherRestTemplate;
import pl.joboffers.infrastructure.offer.http.OfferFetcherRestTemplateConfigurationProperties;


public class OfferFetcherRestTemplateErrorIntegrationTestConfig extends OfferFetcherClientConfig {

    public OfferFetchable remoteOfferFetcherClient(int port, int connectionTimeout, int readTimeout) {
        RestTemplate restTemplate = restTemplate(connectionTimeout, readTimeout, restTemplateResponseErrorHandler());
        String uri = "http://localhost";
        return new OfferFetcherRestTemplate(restTemplate, uri, port);
    }
}
