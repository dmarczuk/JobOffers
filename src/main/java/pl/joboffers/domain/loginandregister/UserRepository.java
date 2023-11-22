package pl.joboffers.domain.loginandregister;

interface UserRepository {
    User save(User user);

    User findByUsername(String username);
}
