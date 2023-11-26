package pl.joboffers.domain.loginandregister;

import pl.joboffers.domain.loginandregister.exception.UserRegistrationException;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepositoryTestImpl implements UserRepository {
    Map<String, User> inMemoryDatabase = new ConcurrentHashMap<>();
    @Override
    public User save(User user) {
        if(inMemoryDatabase.containsKey(user.username())) {
            throw new UserRegistrationException("User cannot register");
        } else {
            inMemoryDatabase.put(user.username(), user);
            return user;
        }
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return Optional.ofNullable(inMemoryDatabase.get(username));
    }
}
