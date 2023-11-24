package pl.joboffers.domain.offer.exceptions;

public class OfferNotFound extends RuntimeException {
    public OfferNotFound(String offerNotFound) {
        super(offerNotFound);
    }
}
