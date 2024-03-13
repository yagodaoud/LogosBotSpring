package yagodaoud.com.logos.music;

public class VolumeService {
    public void setVolume(GuildMusicManager musicManager, int volume) {
        musicManager.player.setVolume(volume);
    }
}
