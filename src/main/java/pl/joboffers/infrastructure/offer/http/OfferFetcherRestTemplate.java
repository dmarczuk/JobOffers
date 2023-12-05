package pl.joboffers.infrastructure.offer.http;

import lombok.AllArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import pl.joboffers.domain.offer.OfferFetchable;
import pl.joboffers.domain.offer.dto.JobOfferResponse;

import java.util.Set;

@AllArgsConstructor
public class OfferFetcherRestTemplate implements OfferFetchable {

    private final RestTemplate restTemplate;
    private final String uri;
    private final int port;
    @Override
    public Set<JobOfferResponse> fetchOffers() {
//        String url = "http://ec2-3-120-147-150.eu-central-1.compute.amazonaws.com:5057/offers";
        String url = getUrlForService("/offers");
        HttpHeaders headers = new HttpHeaders();
        // to do: refactor (add try catch)
        final HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Set<JobOfferResponse>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });
        return response.getBody();
    }

    private String getUrlForService(String service) {
        return uri + ":" + port + service;
    }
}
