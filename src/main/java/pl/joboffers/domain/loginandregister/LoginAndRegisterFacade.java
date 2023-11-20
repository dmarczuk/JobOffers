package pl.joboffers.domain.loginandregister;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LoginAndRegisterFacade {

    private final UserValidator userValidator;
    private List<User> listOfUsers;

    public LoginAndRegisterFacade(UserValidator userValidator) {
        this.userValidator = userValidator;
        this.listOfUsers = new ArrayList<>();
    }

    public String register(User user) {
        if(findByUsername(user.getUsername()).isPresent()) {
            return "User exist in database";
        }
        if(userValidator.hasCorrectAllArguments(user)) {
            listOfUsers.add(user);
            return "success";
        } else {
            return "invalid arguments";
        }
    }

    public Optional<User> findByUsername(String username) {
        return listOfUsers.stream()
                .filter(user -> user.getUsername().equals(username))
                .findAny();
        //return new User("username", "pass", "email");
    }
}
