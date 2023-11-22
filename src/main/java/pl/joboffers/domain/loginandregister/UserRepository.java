package pl.joboffers.domain.loginandregister;

import java.util.Optional;

interface UserRepository {
    User save(User user);

    Optional<User> findByUsername(String username);
}
