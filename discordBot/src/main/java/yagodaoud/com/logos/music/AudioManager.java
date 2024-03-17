package yagodaoud.com.logos.music;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;

public class AudioManager {

    private static AudioManager INSTANCE;
    public final AudioPlayer audioPlayer;
    public final TrackScheduler scheduler;
    public final AudioPlayerSendHandler sendHandler;

    public AudioManager(AudioPlayerManager manager) {
        this.audioPlayer = manager.createPlayer();
        this.scheduler = new TrackScheduler(this.audioPlayer);
        this.audioPlayer.addListener(this.scheduler);
        this.sendHandler = new AudioPlayerSendHandler(this.audioPlayer);
    }

    public AudioPlayerSendHandler getSendHandler() {
        return sendHandler;
    }

    public static AudioManager getInstance() {
        return INSTANCE;
    }

    public static AudioManager getInstance(AudioPlayerManager audioPlayerManager) {
        if (INSTANCE == null) {
            INSTANCE = new AudioManager(audioPlayerManager);
        }
        return INSTANCE;
    }

}