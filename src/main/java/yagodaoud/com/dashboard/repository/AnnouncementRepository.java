package yagodaoud.com.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yagodaoud.com.dashboard.model.entity.Announcement;

public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {
}
