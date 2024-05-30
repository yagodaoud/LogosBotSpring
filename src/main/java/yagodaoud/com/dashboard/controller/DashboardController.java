package yagodaoud.com.dashboard.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import yagodaoud.com.logos.botBuilder.DiscordBotInitializer;

@Controller
public class DashboardController {
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
        TextChannel textChannel = discordBot.getTextChannelById("1019744892290879498");
        textChannel.sendMessage(message).queue();
        return "{\"status\":\"Announcement sent: " + message + "\"}";
    }
}

