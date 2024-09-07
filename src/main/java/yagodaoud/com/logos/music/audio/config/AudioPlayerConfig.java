package yagodaoud.com.logos.music.audio.config;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.repository.cdi.Eager;

@Configuration
@Eager
public class AudioPlayerConfig {

    @Bean
    public AudioPlayerManager audioPlayerManager() {
        AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();
        YoutubeAudioSourceManager ytSourceManager = new YoutubeAudioSourceManager();
        audioPlayerManager.registerSourceManager(ytSourceManager);
        AudioSourceManagers.registerLocalSource(audioPlayerManager);
        return audioPlayerManager;
    }
}
