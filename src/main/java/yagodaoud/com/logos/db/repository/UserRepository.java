package yagodaoud.com.logos.db.repository;

import org.springframework.data.repository.CrudRepository;
import yagodaoud.com.logos.db.entity.User;

public interface UserRepository extends CrudRepository<User, Integer> {
}
