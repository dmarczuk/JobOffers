package pl.joboffers.http.error;

import lombok.AllArgsConstructor;
import lombok.Builder;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import pl.joboffers.domain.offer.OfferFetchable;
import pl.joboffers.infrastructure.offer.http.OfferFetcherClientConfig;
import pl.joboffers.infrastructure.offer.http.OfferFetcherRestTemplate;
import pl.joboffers.infrastructure.offer.http.OfferFetcherRestTemplateConfigurationProperties;


@Configuration
public class OfferFetcherRestTemplateErrorIntegrationTestConfig extends OfferFetcherClientConfig {

    public OfferFetcherRestTemplateErrorIntegrationTestConfig(OfferFetcherRestTemplateConfigurationProperties properties) {
        super(properties);
    }



    public OfferFetchable remoteOfferFetcherClient(int port) {
        RestTemplate restTemplate = restTemplate(restTemplateResponseErrorHandler());
        String uri = "http://localhost";
        return new OfferFetcherRestTemplate(restTemplate, uri, port);
    }
}
