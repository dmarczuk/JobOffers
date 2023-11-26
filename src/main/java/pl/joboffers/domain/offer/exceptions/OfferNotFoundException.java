package pl.joboffers.domain.offer.exceptions;

public class OfferNotFoundException extends RuntimeException {
    public OfferNotFoundException(String offerNotFound) {
        super(offerNotFound);
    }
}
