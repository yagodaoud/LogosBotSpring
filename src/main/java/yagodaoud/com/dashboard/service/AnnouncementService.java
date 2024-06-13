package yagodaoud.com.dashboard.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import yagodaoud.com.dashboard.entity.Announcement;
import yagodaoud.com.dashboard.repository.AnnouncementRepository;

import java.time.LocalDateTime;

@Service
public class AnnouncementService {
    @Autowired
    private AnnouncementRepository announcementRepository;

    @Async
    public void insertAnnouncementAsync(String message) {
        Announcement announcement = new Announcement();
        announcement.setAnnouncementMessage(message);
        announcement.setDateAdded(LocalDateTime.now());
        announcementRepository.save(announcement);
    }
}
