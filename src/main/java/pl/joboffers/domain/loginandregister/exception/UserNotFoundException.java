package pl.joboffers.domain.loginandregister.exception;

public class UserNotFoundException extends RuntimeException {

    public UserNotFoundException(String userNotFound) {
        super(userNotFound);
    }
}
