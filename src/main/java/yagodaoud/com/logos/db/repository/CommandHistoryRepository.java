package yagodaoud.com.logos.db.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import yagodaoud.com.logos.db.entity.CommandHistory;

@Repository
public interface CommandHistoryRepository extends CrudRepository<CommandHistory, Integer> {

}
