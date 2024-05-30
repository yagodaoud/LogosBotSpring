package yagodaoud.com.logos.db.repository;

import jakarta.transaction.Transactional;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.cdi.Eager;
import org.springframework.stereotype.Repository;
import yagodaoud.com.logos.db.entity.AnnouncementChannel;

@Repository
@Eager
public interface AnnouncementChannelRepository extends CrudRepository<AnnouncementChannel, Integer> {
    AnnouncementChannel findByGuildId(String guildId);

    AnnouncementChannel findByChannelIdAndGuildId(String channelId, String guildId);
    @Transactional
    void deleteByChannelId(String channelId);
}
