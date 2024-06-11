package yagodaoud.com.dashboard.controller;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.managers.AudioManager;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import yagodaoud.com.logos.botBuilder.DiscordBotInitializer;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@Lazy
public class VoiceController {
    private final JDA discordBot = DiscordBotInitializer.getDiscordBot();

    @GetMapping("/voice")
    public String voice(Model model) {
        List<AudioChannelUnion> voiceChannels = getAllConnectedChannels(discordBot);
        System.out.println(voiceChannels.isEmpty());
        model.addAttribute("voiceChannels", voiceChannels);
        return "voice";
    }

    @PostMapping("/voice/disconnectVoiceChannel")
    @ResponseBody
    public String disconnectVoiceChannel(@RequestBody String channelId) {

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

    public List<AudioChannelUnion> getAllConnectedChannels(JDA bot) {
        return bot.getAudioManagers().stream()
                .map(AudioManager::getConnectedChannel)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }
}
