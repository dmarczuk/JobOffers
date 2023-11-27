package pl.joboffers.domain.offer.exceptions;


public class SaveOfferException extends RuntimeException {
    public SaveOfferException(String message) {
        super(message);
    }
}
