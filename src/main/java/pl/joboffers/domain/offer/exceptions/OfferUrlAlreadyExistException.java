package pl.joboffers.domain.offer.exceptions;

public class OfferUrlAlreadyExistException extends RuntimeException {
    public OfferUrlAlreadyExistException(String message) {
        super(message);
    }
}
