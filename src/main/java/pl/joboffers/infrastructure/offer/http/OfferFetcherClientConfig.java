package pl.joboffers.infrastructure.offer.http;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import pl.joboffers.domain.offer.OfferFetchable;

import java.time.Duration;

@Configuration
public class OfferFetcherClientConfig {

    @Bean
    public RestTemplateResponseErrorHandler restTemplateResponseErrorHandler() {
        return new RestTemplateResponseErrorHandler();
    }

    @Bean
    public RestTemplate restTemplate(@Value("${joboffers.offer.http.client.config.connectionTimeout:1000}") long connectionTimeout,
                                     @Value("${joboffers.offer.http.client.config.readTimeout:1000}") long readTimeout,
                                     RestTemplateResponseErrorHandler restTemplateResponseErrorHandler) {
        return new RestTemplateBuilder()
                .errorHandler(restTemplateResponseErrorHandler)
                .setConnectTimeout(Duration.ofMillis(connectionTimeout))
                .setReadTimeout(Duration.ofMillis(readTimeout))
                .build();
    }

    @Bean
    public OfferFetchable remoteOfferClient(RestTemplate restTemplate,
                                            @Value("${joboffers.offer.http.client.config.uri:http://example.com}") String uri,
                                            @Value("${joboffers.offer.http.client.config.port:5057}") int port) {
        return new OfferFetcherRestTemplate(restTemplate, uri, port);
    }

}
