package pl.joboffers.domain.offer.exceptions;

public class OfferDuplicateException extends RuntimeException {
    public OfferDuplicateException(String message) {
        super(message);
    }
}
