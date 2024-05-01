package yagodaoud.com.logos.db.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import yagodaoud.com.logos.db.entity.User;

@Repository
public interface UserRepository extends CrudRepository<User, Integer> {
    User findByDiscordId(Long discordId);
}
