package yagodaoud.com.dashboard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import yagodaoud.com.dashboard.helper.DefaultAnnouncementView;
import yagodaoud.com.logos.botBuilder.DiscordBotInitializer;
import yagodaoud.com.logos.db.entity.AnnouncementChannel;
import yagodaoud.com.logos.db.repository.AnnouncementChannelRepository;
import yagodaoud.com.logos.tools.Colors;

import java.util.ArrayList;
import java.util.List;

import static yagodaoud.com.logos.tools.MessageEmbedBuilder.messageEmbedBuilder;

@Controller
public class DashboardController {

    private final AnnouncementChannelRepository announcementChannelRepository;

    @Autowired
    public DashboardController(AnnouncementChannelRepository announcementChannelRepository) {
        this.announcementChannelRepository = announcementChannelRepository;
    }
    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }

    @PostMapping("/dashboard/announce")
    @ResponseBody
    public String sendAnnouncement(@RequestBody String announcement) {
        String message = "";
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode jsonNode = objectMapper.readTree(announcement);
            message = jsonNode.get("message").asText();
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        JDA discordBot = DiscordBotInitializer.getDiscordBot();

        sendAnnouncementOnAllChannels(discordBot, message);

        return "{\"status\":\"Announcement sent: " + message + "\"}";
    }

    public void sendAnnouncementOnAllChannels(JDA bot, String message) {
        List<AnnouncementChannel> channelList = (ArrayList<AnnouncementChannel>) announcementChannelRepository.findAll();
        channelList.forEach(c -> bot.getTextChannelById(c.getChannelId()).sendMessage(DefaultAnnouncementView.getDefaultAnnouncementView(bot, message)).queue());
    }

}

