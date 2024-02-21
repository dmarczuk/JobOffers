package pl.joboffers.domain.loginandregister;

import com.mongodb.client.MongoDatabase;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
interface UserRepository extends MongoRepository<User, String> {
//    User save(User user);

    Optional<User> findByUsername(String username);
}
