package yagodaoud.com.logos.music.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue = new LinkedBlockingQueue<>();
    private boolean loop = false;

    public TrackScheduler(AudioPlayer player) {
        this.player = player;
    }

    public void queue(AudioTrack track, boolean forcePlay) {
        System.out.println(track.getInfo().title);

        if (forcePlay) {
            this.player.startTrack(track, false);
            return;
        }
        if (!this.player.startTrack(track, true)) {
            this.queue.add(track);
        }
    }

    public String nextTrack() {
        if (this.player.isPaused()){
            this.player.setPaused(false);
        }

        AudioTrack nextTrack;

        nextTrack = this.player.getPlayingTrack().makeClone();

        if (!this.loop) {
            nextTrack = this.queue.poll();
        }

        if (this.player.getPlayingTrack() == null) {
            return "The queue is empty.";
        }
        if (nextTrack == null) {
            this.player.stopTrack();
            this.queue.clear();
            return "Skipped current track, the queue is now empty.";
        }
        this.player.startTrack(nextTrack, false);
        return "Skipped to the next track.";
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        if (endReason.toString().equals("FINISHED") || endReason.mayStartNext) {
            AudioTrack nextTrack;

            nextTrack = track.makeClone();
            if (!loop) {
                nextTrack = queue.poll();
            }
            this.player.startTrack(nextTrack, false);
        }
    }

    public String nowPlaying() {
        return this.getNowPlayingMessage(this.player.getPlayingTrack(), false);
    }

    public String getQueue() {
        return this.getQueueTracksMessage(this.player.getPlayingTrack(), this.queue);
    }

    public String stopPlayer() {
        if (this.player.getPlayingTrack() == null) {
            return "Nothing is being played right now.";
        }
        if (this.player.isPaused()) {
            return "The player is already paused.";
        }
        this.player.setPaused(true);
        return "Stopped the player.";
    }

    public String resumePlayer() {
        if (this.player.getPlayingTrack() == null) {
            return "Nothing is being played right now.";
        }
        if (!this.player.isPaused()) {
            return "The player is already resumed.";
        }
        this.player.setPaused(false);
        return "Resumed the player.";
    }

    public String clearQueue() {
        if (this.player.getPlayingTrack() == null) {
            return "Nothing is being played right now.";
        }
        if (this.queue.isEmpty()) {
            return "Queue already empty.";
        }
        this.player.stopTrack();
        this.queue.clear();
        return "The queue is now empty.";
    }

    public String jumpTo(int trackNumber) {
        if (this.player.getPlayingTrack() == null) {
            return "Nothing is being played right now.";
        }
        if (trackNumber <= 0 || trackNumber > queue.size()) {
            return "Track number out of range.";
        }
        for (int i = 1; i < trackNumber - 1; i++) {
            queue.poll();
        }
        AudioTrack skippedToTrack = queue.poll();
        this.player.startTrack(skippedToTrack, false);
        return "Skipped to: " + getNowPlayingMessage(skippedToTrack, true);
    }

    public String loopQueue() {
        if (this.player.getPlayingTrack() == null) {
            return "Nothing is being played right now.";
        }

        if (loop) {
            loop = false;
            return "Loop is off!";
        }

        loop = true;
        return "Loop is on!";
    }

    public String getNowPlayingMessage(AudioTrack audioTrack, boolean removeNowPlaying) {
        if (audioTrack == null) {
            return "Nothing is being played right now.";
        }

        return (removeNowPlaying ? "[" : "Now playing: [") +
                audioTrack.getInfo().title +
                "](" + audioTrack.getInfo().uri + ")  -  `" +
                formatDuration(audioTrack.getDuration()) +
                "`";
    }

    public String getQueueTracksMessage(AudioTrack firstTrack, BlockingQueue<AudioTrack> queue) {
        if (firstTrack == null) {
            return "Nothing is being played right now";
        }

        if (queue.isEmpty()) {
            return "The queue is empty.";
        }

        StringBuilder message = new StringBuilder();
        message.append("Queue\n\n Now playing - [").append(firstTrack.getInfo().title).append("](").append(firstTrack.getInfo().uri).append(") - `").append(formatDuration(firstTrack.getDuration())).append("`\n\n Next tracks: \n");

        int trackNumber = 1;
        for (AudioTrack track : queue) {
            if (trackNumber > 10) {
                break;
            }
            message.append(trackNumber)
                    .append(" - ")
                    .append(getNowPlayingMessage(track, true))
                    .append("\n");
            trackNumber++;
        }

        return message.toString();
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
