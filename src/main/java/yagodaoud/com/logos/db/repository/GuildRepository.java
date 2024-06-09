package yagodaoud.com.logos.db.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import yagodaoud.com.logos.db.entity.Guild;

public interface GuildRepository extends JpaRepository<Guild, Integer> {
}
