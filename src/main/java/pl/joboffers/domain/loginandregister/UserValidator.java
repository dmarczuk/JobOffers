package pl.joboffers.domain.loginandregister;

class UserValidator {

    public boolean hasCorrectAllArguments(User user) {
        if (!hasCorrectUsername(user.getUsername())) {
            return false;
        }
        if (!hasCorrectPassword(user.getPassword())) {
            return false;
        }
        return hasCorrectEmail(user.getEmail());

    }

    private boolean hasCorrectUsername(String username) {
        return !username.equals("");
    }

    private boolean hasCorrectPassword(String password) {
        return !password.equals("");
    }

    private boolean hasCorrectEmail(String email) {
        return !email.equals("");
    }


}
