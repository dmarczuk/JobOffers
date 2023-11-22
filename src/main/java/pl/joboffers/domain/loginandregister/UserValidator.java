package pl.joboffers.domain.loginandregister;

class UserValidator {

    boolean hasCorrectAllArguments(User user) {
        if (!hasCorrectUsername(user.getUsername())) {
            return false;
        }
        if (!hasCorrectPassword(user.getPassword())) {
            return false;
        }
        return hasCorrectEmail(user.getEmail());

    }

    private boolean hasCorrectUsername(String username) {  //throw new UsernameInvalidException
        return !username.equals("");
    }

    private boolean hasCorrectPassword(String password) {  //throw new UsernameInvalidException
        return !password.equals("");
    }

    private boolean hasCorrectEmail(String email) {  //throw new UsernameInvalidException
        return !email.equals("");
    }


}
