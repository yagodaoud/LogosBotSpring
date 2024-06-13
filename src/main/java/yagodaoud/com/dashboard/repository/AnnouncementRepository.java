package yagodaoud.com.dashboard.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import yagodaoud.com.dashboard.entity.Announcement;

import java.util.List;

public interface AnnouncementRepository extends JpaRepository<Announcement, Integer> {
    List<Announcement> findTopByOrderByIdDesc();
}
