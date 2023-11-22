package pl.joboffers.domain.loginandregister;

import lombok.RequiredArgsConstructor;
import pl.joboffers.domain.loginandregister.dto.RegisterUserDto;
import pl.joboffers.domain.loginandregister.dto.RegistrationResultDto;
import pl.joboffers.domain.loginandregister.dto.UserDto;
import pl.joboffers.domain.loginandregister.exception.UserNotFoundException;

@RequiredArgsConstructor
public class LoginAndRegisterFacade {

    private final UserValidator userValidator;
    private final UserRepository userRepository;
    //private List<User> listOfUsers;

//    public LoginAndRegisterFacade(UserValidator userValidator) {
//        this.userValidator = userValidator;
//        this.listOfUsers = new ArrayList<>();
//    }

    public RegistrationResultDto register(RegisterUserDto registerUserDto) {
//        if(findByUsername(user.getUsername()) != null) {
//            return "User exist in database";
//        }
        findByUsername(user.getUsername());
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

    public UserDto findByUsername(String username) {
        User user =  userRepository.findByUsername(username);
        if (user != null) {
            return user;
        } else {
            throw new UserNotFoundException("User not found in database");
        }
//                .filter(user -> user.getUsername().equals(username))
//                .findAny()
//                .orElse(null);
        //return new User("username", "pass", "email");
    }
}
