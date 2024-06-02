package yagodaoud.com.dashboard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.managers.AudioManager;
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
import yagodaoud.com.logos.db.repository.GuildRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@Lazy
public class DashboardController {

    @Autowired
    private AnnouncementChannelRepository announcementChannelRepository;
    @Autowired
    private GuildRepository guildRepository;
    private final JDA discordBot = DiscordBotInitializer.getDiscordBot();

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        List<yagodaoud.com.logos.db.entity.Guild> guilds = (ArrayList<yagodaoud.com.logos.db.entity.Guild>) guildRepository.findAll();
        List<AudioChannelUnion> audioManagers = getAllConnectedChannels(discordBot);
        model.addAttribute("guilds", guilds);
        model.addAttribute("audioManagers", audioManagers);
        return "dashboard";
    }

    @PostMapping("/dashboard/updateGuilds")
    @ResponseBody
    public List<String> updateGuilds() {
        return getAllGuilds(discordBot);
    }
    @PostMapping("/dashboard/disconnectVoiceChannel")
    @ResponseBody
    public String disconnectVoiceChannel(@RequestBody String channelId) {
        System.out.println(channelId);

        Optional<AudioManager> optionalAudioManager = discordBot.getAudioManagers().stream()
                .filter(am -> am.getConnectedChannel() != null && am.getConnectedChannel().getId().equals(channelId))
                .findFirst();

        if (optionalAudioManager.isEmpty()) {
            return "{\"status\":\"No connected channel found with the given ID\"}";
        }

        AudioManager audioManager = optionalAudioManager.get();
        String channelName = audioManager.getConnectedChannel().getName();
        audioManager.closeAudioConnection();

        return "{\"status\":\"Disconnected from: " + channelName + "\"}";
    }


    @PostMapping("/dashboard/announce")
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

    public List<String> getAllGuilds(JDA bot) {
        List<Guild> guilds = bot.getGuilds();

        guildRepository.deleteAll();

        guilds.forEach(guild -> {
            yagodaoud.com.logos.db.entity.Guild guildEntity = new yagodaoud.com.logos.db.entity.Guild();
            guildEntity.setGuildId(guild.getId());
            guildEntity.setGuildName(guild.getName());
            guildEntity.setGuildDescription(guild.getDescription());
            guildEntity.setGuildIconUrl(guild.getIconUrl());
            guildEntity.setGuildMemberCount(guild.getMemberCount());

            guildRepository.save(guildEntity);
        });

        return guilds.stream().map(Guild::getName).toList();
    }

    public List<AudioChannelUnion> getAllConnectedChannels(JDA bot) {
        return bot.getAudioManagers().stream()
                .map(AudioManager::getConnectedChannel)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public void sendAnnouncementOnAllChannels(JDA bot, String message) {
        List<AnnouncementChannel> channelList = (ArrayList<AnnouncementChannel>) announcementChannelRepository.findAll();
        channelList.forEach(c -> bot.getTextChannelById(c.getChannelId()).sendMessage(DefaultAnnouncementView.getDefaultAnnouncementView(bot, message)).queue());
    }
}
