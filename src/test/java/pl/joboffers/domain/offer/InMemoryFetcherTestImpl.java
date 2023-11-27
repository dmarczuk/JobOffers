package pl.joboffers.domain.offer;

import pl.joboffers.domain.offer.dto.JobOfferResponse;

import java.util.List;
import java.util.Set;

public class InMemoryFetcherTestImpl implements OfferFetchable {
    private Set<JobOfferResponse> listOffers;

    public InMemoryFetcherTestImpl(Set<JobOfferResponse> listOffers) {
        this.listOffers = listOffers;
    }

    @Override
    public Set<JobOfferResponse> fetchOffers() {
        return listOffers;
    }
}
