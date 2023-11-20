package pl.joboffers.domain.loginandregister;

class UserValidator {

    public boolean hasCorrectAllArguments(User user) {
        if (!hasCorrectUsername()){
            return false;
        }
        if (!hasCorrectPassword()){
            return false;
        }
        if (!hasCorrectEmail()){
            return false;
        }
        return true;

    }

    private boolean hasCorrectUsername() {

        return true;
    }

    private boolean hasCorrectPassword() {

        return true;
    }

    private boolean hasCorrectEmail() {

        return true;
    }


}
