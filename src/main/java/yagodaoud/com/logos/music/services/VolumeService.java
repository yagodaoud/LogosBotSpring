package yagodaoud.com.logos.music.services;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import org.springframework.stereotype.Service;

@Service
public class VolumeService {
    public static String setVolume(AudioPlayer player, int volume) {

        if (volume > 100) {
            volume = 100;
        }

        if (volume < 1) {
            volume = 1;
        }

        player.setVolume(volume);
        return "Volume set to " + volume + ".";
    }
}
