package yagodaoud.com.dashboard.entity;

import jakarta.persistence.*;
import org.hibernate.annotations.GenericGenerator;

import java.time.LocalDateTime;


@Entity
public class Announcement {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @GenericGenerator(name = "native")
    private Integer id;
    private String announcementMessage;
    private LocalDateTime dateAdded;
    @Transient
    private String formattedDateAdded;

    public String getAnnouncementMessage() {
        return announcementMessage;
    }

    public void setAnnouncementMessage(String announcementMessage) {
        this.announcementMessage = announcementMessage;
    }

    public LocalDateTime getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(LocalDateTime dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getFormattedDateAdded() {
        return formattedDateAdded;
    }

    public void setFormattedDateAdded(String formattedDateAdded) {
        this.formattedDateAdded = formattedDateAdded;
    }
}
