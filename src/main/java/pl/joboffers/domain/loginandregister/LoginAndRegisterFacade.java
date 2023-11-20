package pl.joboffers.domain.loginandregister;

import java.util.ArrayList;
import java.util.List;

public class LoginAndRegisterFacade {

    private final UserValidator userValidator;
    private List<User> listOfUsers = new ArrayList<>();

    public LoginAndRegisterFacade(UserValidator userValidator) {
        this.userValidator = userValidator;
    }

    public void register(User user) {
        listOfUsers.add(user);

    }

    public User findByUsername(String username) {
        return new User("username", "pass", "email");
    }
}
