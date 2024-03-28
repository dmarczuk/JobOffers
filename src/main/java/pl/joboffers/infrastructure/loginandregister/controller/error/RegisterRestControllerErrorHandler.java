package pl.joboffers.infrastructure.loginandregister.controller.error;

import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import pl.joboffers.domain.loginandregister.exception.UserAlreadyExistException;

@ControllerAdvice
@Log4j2
public class RegisterRestControllerErrorHandler {

    @ExceptionHandler(UserAlreadyExistException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.CONFLICT)
    public UserExistErrorResponse userAlreadyExistInDatabase(UserAlreadyExistException exception) {
        String message = "User already exist in database";
        log.error(exception.getMessage());
        return new UserExistErrorResponse(message, HttpStatus.CONFLICT);
    }
}
