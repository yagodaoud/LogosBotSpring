package yagodaoud.com.dashboard.service;

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.managers.AudioManager;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class VoiceService {

    public List<AudioChannelUnion> getAllConnectedChannels(JDA bot) {
        return bot.getAudioManagers().stream()
                .map(AudioManager::getConnectedChannel)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    public int countAllConnectedChannels(JDA bot) {
        return bot.getAudioManagers().stream()
                .map(AudioManager::getConnectedChannel)
                .filter(Objects::nonNull)
                .toList().size();
    }
}
