package yagodaoud.com.logos.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import org.springframework.stereotype.Service;

@Service
public class VolumeService {
    public static void setVolume(AudioPlayer player, int volume) {
        player.setVolume(volume);
    }
}
