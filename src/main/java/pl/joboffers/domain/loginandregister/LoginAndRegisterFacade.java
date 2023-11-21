package pl.joboffers.domain.loginandregister;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
public class LoginAndRegisterFacade {

    private final UserValidator userValidator;
    private final UserRepository userRepository;
    //private List<User> listOfUsers;

//    public LoginAndRegisterFacade(UserValidator userValidator) {
//        this.userValidator = userValidator;
//        this.listOfUsers = new ArrayList<>();
//    }

    public String register(User user) {
        if(findByUsername(user.getUsername()) != null) {
            return "User exist in database";
        }
        if(userValidator.hasCorrectAllArguments(user)) {
            User savedUser = userRepository.save(user);  // new Record (5.04 6min)
            //listOfUsers.add(user);
            return "success";   // responseRegister dto (5.3 9min)
        } else {
            return "invalid arguments";
        }
    }

    public String login(String username, String password) {
        User user = findByUsername(username);
        if (user != null && user.getPassword().equals(password)) {
            return "Successful login";
        } else {
            return "Invalid username or password";
        }
    }

    public User findByUsername(String username) {
        return userRepository.findByUsername(username);
//                .filter(user -> user.getUsername().equals(username))
//                .findAny()
//                .orElse(null);
        //return new User("username", "pass", "email");
    }
}
