package yagodaoud.com.logos.music.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {

    private final AudioPlayer player;
    private BlockingQueue<AudioTrack> queue = new LinkedBlockingQueue<>();

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
    }

    public void queue(AudioTrack track) {
        System.out.println(track.getInfo().title);

        if (!this.player.startTrack(track, true)) {
            this.queue.add(track);
        }
    }

    public String nextTrack() {
//        if (player.isPaused()){
//            player.setPaused(false);
//        }

        AudioTrack nextTrack = queue.poll();

        if (player.getPlayingTrack() == null) {
            return "The queue is empty.";
        }

        if (nextTrack == null) {
            player.stopTrack();
            queue.clear();
            return "Skipped current track, the queue is now empty.";
        }

        this.player.startTrack(nextTrack, false);
        return "Skipped to the next track.";


    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.toString().equals("FINISHED") || endReason.mayStartNext) {
            this.player.startTrack(queue.poll(), false);
        }
    }

    public String nowPlaying() {
        return "Now playing: " + this.getNowPlayingMessage(this.player.getPlayingTrack());
    }

    public String getNowPlayingMessage(AudioTrack audioTrack) {
        return "`" + audioTrack.getInfo().title +
                " (" +
                formatDuration(audioTrack.getDuration()) +
                ")` by `" +
                audioTrack.getInfo().author +
                "`";
    }

    private String formatDuration(long durationMs) {
        long seconds = (durationMs / 1000) % 60;
        long minutes = (durationMs / (1000 * 60)) % 60;
        long hours = (durationMs / (1000 * 60 * 60)) % 24;

        if (hours > 0) {
            return String.format("%02d:%02d:%02d", hours, minutes, seconds);
        }
        return String.format("%02d:%02d", minutes, seconds);
    }

}
