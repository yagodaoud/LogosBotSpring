package yagodaoud.com.dashboard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import yagodaoud.com.dashboard.helper.DefaultAnnouncementView;
import yagodaoud.com.logos.botBuilder.DiscordBotInitializer;
import yagodaoud.com.logos.db.entity.AnnouncementChannel;
import yagodaoud.com.logos.db.repository.AnnouncementChannelRepository;

import java.util.ArrayList;
import java.util.List;

@Controller
@Lazy
public class AnnouncementController {

    @Autowired
    private AnnouncementChannelRepository announcementChannelRepository;

    private final JDA discordBot = DiscordBotInitializer.getDiscordBot();

    @GetMapping("/announcements")
    public String announcement() {
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

        return "{\"status\":\"Announcement sent: " + message + "\"}";
    }

    public void sendAnnouncementOnAllChannels(JDA bot, String message) {
        List<AnnouncementChannel> channelList = (ArrayList<AnnouncementChannel>) announcementChannelRepository.findAll();
        channelList.forEach(c -> bot.getTextChannelById(c.getChannelId()).sendMessage(DefaultAnnouncementView.getDefaultAnnouncementView(bot, message)).queue());
    }
}
