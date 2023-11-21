package pl.joboffers.domain.loginandregister;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InMemoryUserRepositoryTestImpl implements UserRepository {
    Map<String, User> inMemoryDatabase = new ConcurrentHashMap<>();
    @Override
    public User save(User user) {
        inMemoryDatabase.put(user.getUsername(), user);
        return user;
    }

    @Override
    public User findByUsername(String username) {
        return inMemoryDatabase.get(username);
    }
}
