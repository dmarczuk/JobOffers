package pl.joboffers.infrastructure.offer.controller.error;

import com.mongodb.DuplicateKeyException;
import lombok.extern.java.Log;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.joboffers.domain.offer.exceptions.OfferNotFoundException;

@ControllerAdvice
@Log4j2
public class OfferRestControllerErrorHandler {

    @ExceptionHandler(OfferNotFoundException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public OfferNotFoundErrorResponse offerNotFound(OfferNotFoundException exception) {
        String message = exception.getMessage();
        log.error(message);
        return new OfferNotFoundErrorResponse(message, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(DuplicateKeyException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public OfferUrlExistErrorResponse offerUrlAlreadyExistInDatabase(DuplicateKeyException exception) {
        String message = exception.getMessage();
        log.error(message);
        return new OfferUrlExistErrorResponse(message, HttpStatus.NOT_FOUND);
    }
}
