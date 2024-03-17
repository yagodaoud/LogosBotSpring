package yagodaoud.com.logos.music.services;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import org.springframework.stereotype.Service;

@Service
public class VolumeService {
    public static String setVolume(AudioPlayer player, int volume) {
        player.setVolume(volume);
        return "Volume set to " + volume + ".";
    }
}
