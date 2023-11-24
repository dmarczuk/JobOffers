package pl.joboffers.domain.loginandregister.exception;

public class UserRegistrationException extends RuntimeException {
    public UserRegistrationException(String userNotRegister) {
        super(userNotRegister);
    }
}
