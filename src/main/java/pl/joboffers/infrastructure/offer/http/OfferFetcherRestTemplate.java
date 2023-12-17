package pl.joboffers.infrastructure.offer.http;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import pl.joboffers.domain.offer.OfferFetchable;
import pl.joboffers.domain.offer.dto.JobOfferResponse;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Set;

@AllArgsConstructor
@Log4j2
public class OfferFetcherRestTemplate implements OfferFetchable {

    private final RestTemplate restTemplate;
    private final String uri;
    private final int port;
    @Override
    public List<JobOfferResponse> fetchOffers() {
        //String url = "http://ec2-3-120-147-150.eu-central-1.compute.amazonaws.com:5057/offers";
        log.info("Started fetching offers");
        HttpHeaders headers = new HttpHeaders();
        final HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);
        try {
            String url = getUrlForService("/offers");
            ResponseEntity<List<JobOfferResponse>> response = restTemplate.exchange(url, HttpMethod.GET, requestEntity,
                    new ParameterizedTypeReference<>() {
                    });
            final List<JobOfferResponse> body = response.getBody();
            if (body == null) {
                log.info("Response body was null");
                return Collections.emptyList();
            }
            return body;
        } catch (ResourceAccessException e) {
            log.error("Error while fetching offers: " + e.getMessage());
            return Collections.emptyList();
        }
   }

    private String getUrlForService(String service) {
        return uri + ":" + port + service;
    }
}
