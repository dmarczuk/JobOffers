package pl.joboffers.domain.offer;

import pl.joboffers.domain.offer.dto.JobOfferResponse;

import java.util.List;
import java.util.Set;

public interface OfferFetchable {
    Set<JobOfferResponse> fetchOffers();
}
