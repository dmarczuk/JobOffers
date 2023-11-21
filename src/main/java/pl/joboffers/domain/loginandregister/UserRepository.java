package pl.joboffers.domain.loginandregister;

public interface UserRepository {
    User save(User user);

    User findByUsername(String username);
}
