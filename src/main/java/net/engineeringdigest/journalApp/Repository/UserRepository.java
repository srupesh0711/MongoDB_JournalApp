package net.engineeringdigest.journalApp.Repository;

import net.engineeringdigest.journalApp.Entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, String> {
    User findByUsername(String username);
}