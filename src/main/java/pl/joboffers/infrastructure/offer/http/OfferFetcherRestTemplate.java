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
@Component
public class OfferFetcherRestTemplate implements OfferFetchable {

    private final RestTemplate restTemplate;
    @Override
    public Set<JobOfferResponse> fetchOffers() {
        String url = "http://ec2-3-120-147-150.eu-central-1.compute.amazonaws.com:5057/offers";
        HttpHeaders headers = new HttpHeaders();
        final HttpEntity<HttpHeaders> requestEntity = new HttpEntity<>(headers);
        ResponseEntity<Set<JobOfferResponse>> response = restTemplate.exchange(
                url,
                HttpMethod.GET,
                requestEntity,
                new ParameterizedTypeReference<>() {
                });
        return response.getBody();
    }
}
