package yagodaoud.com.dashboard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.JDA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import yagodaoud.com.dashboard.helper.DefaultAnnouncementView;
import yagodaoud.com.dashboard.model.entity.Announcement;
import yagodaoud.com.dashboard.repository.AnnouncementRepository;
import yagodaoud.com.logos.botBuilder.DiscordBotInitializer;
import yagodaoud.com.logos.db.entity.AnnouncementChannel;
import yagodaoud.com.logos.db.repository.AnnouncementChannelRepository;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Controller
@Lazy
public class AnnouncementController {

    @Autowired
    private AnnouncementChannelRepository announcementChannelRepository;
    @Autowired
    private AnnouncementRepository announcementRepository;

    private final JDA discordBot = DiscordBotInitializer.getDiscordBot();

    @GetMapping("/announcements")
    public String announcement(Model model) {
        List<Announcement> announcements = this.formatAnnouncementDate(announcementRepository.findAll());

        model.addAttribute("announcements", announcements);
        return "announcements";
    }

    @PostMapping("announcements/announce")
    @ResponseBody
    public String sendGlobalAnnouncement(@RequestBody String announcement) {
        String message = "";
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode jsonNode = objectMapper.readTree(announcement);
            message = jsonNode.get("message").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        sendAnnouncementOnAllChannels(discordBot, message);

        String finalMessage = message;
        new Thread(() -> this.insertAnnouncementAsync(finalMessage)).start();

        return "{\"status\":\"Announcement sent: " + message + "\"}";
    }

    @GetMapping("/announcements/lastAnnouncement")
    @ResponseBody
    public List<Announcement> getLastAnnouncement() {
        return this.formatAnnouncementDate(announcementRepository.findTopByOrderByIdDesc());
    }

    public void sendAnnouncementOnAllChannels(JDA bot, String message) {
        List<AnnouncementChannel> channelList = (ArrayList<AnnouncementChannel>) announcementChannelRepository.findAll();
        channelList.forEach(c -> bot.getTextChannelById(c.getChannelId()).sendMessage(DefaultAnnouncementView.getDefaultAnnouncementView(bot, message)).queue());
    }

    @Async
    private void insertAnnouncementAsync(String message) {
        Announcement announcement = new Announcement();
        announcement.setAnnouncementMessage(message);
        announcement.setDateAdded(LocalDateTime.now());
        announcementRepository.save(announcement);
    }

    private List<Announcement> formatAnnouncementDate(List<Announcement> announcements) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        return announcements.stream().map(a -> {
                    a.setFormattedDateAdded(a.getDateAdded().format(formatter));
                    return a;
        }).collect(Collectors.toList());
    }
}
