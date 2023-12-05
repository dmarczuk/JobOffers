package pl.joboffers.infrastructure.offer.http;

import lombok.Builder;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "joboffers.offer.http.client.config")
@Builder
public record OfferFetcherRestTemplateConfigurationProperties(long connectTimeout, long readTimeout, String uri, int port) {
}
