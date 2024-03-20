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
}
