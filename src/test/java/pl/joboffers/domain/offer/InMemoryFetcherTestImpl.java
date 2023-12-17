package pl.joboffers.domain.offer;

import pl.joboffers.domain.offer.dto.JobOfferResponse;

import java.util.List;
import java.util.Set;

public class InMemoryFetcherTestImpl implements OfferFetchable {
    private List<JobOfferResponse> listOffers;

    public InMemoryFetcherTestImpl(List<JobOfferResponse> listOffers) {
        this.listOffers = listOffers;
    }

    @Override
    public List<JobOfferResponse> fetchOffers() {
        return listOffers;
    }
}
