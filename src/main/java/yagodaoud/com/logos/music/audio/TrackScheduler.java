package yagodaoud.com.logos.music.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.event.AudioEventAdapter;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackEndReason;

import java.time.Duration;
import java.util.*;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicReference;

public class TrackScheduler extends AudioEventAdapter {
    private final AudioPlayer player;
    private final BlockingQueue<AudioTrack> queue = new LinkedBlockingQueue<>();
    private boolean loop = false;
    private boolean shuffle = false;
    public boolean autoplay = false;
    private String currentSongUrl;
    private BlockingQueue<AudioTrack> originalQueue = new LinkedBlockingQueue<>();
    private final GuildMusicManager guildMusicManager;

    public TrackScheduler(AudioPlayer player, GuildMusicManager guildMusicManager) {
        this.player = player;
        this.guildMusicManager = guildMusicManager;
    }

    public void queue(AudioTrack track, boolean forcePlay) {
        System.out.println(track.getInfo().title);
        currentSongUrl = track.getInfo().uri;

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

        if (this.player.getPlayingTrack() == null) {
            return "The queue is empty.";
        }
        AudioTrack nextTrack;

        nextTrack = this.player.getPlayingTrack().makeClone();

        if (!this.loop) {
            nextTrack = this.queue.poll();
        }

        if (nextTrack == null) {
            this.player.stopTrack();
            this.queue.clear();

            if (autoplay) {
                return "Autoplay is looking for the next track.";
            }
            return "Skipped current track, the queue is now empty.";
        }
        this.player.startTrack(nextTrack, false);
        return "Skipped to the next track.";
    }

    @Override
    public void onTrackEnd(AudioPlayer player, AudioTrack track, AudioTrackEndReason endReason) {
        System.out.println(endReason.toString());
        if (endReason.toString().equals("FINISHED") || endReason.mayStartNext || endReason.toString().equals("STOPPED")) {
            AudioTrack nextTrack;

            nextTrack = track.makeClone();

            if (autoplay && queue.size() <= 1) {
                PlayerManager.loadAutoplayTrack(this, PlayerManager.getAudioPlayerManager(), guildMusicManager,
                        new CustomAudioLoadResultHandler(guildMusicManager, "", false, new CompletableFuture<>(), new AtomicReference<>()));
            }
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

    public String skipTo(int trackNumber) {
        if (this.player.getPlayingTrack() == null) {
            return "Nothing is being played right now.";
        }
        if (trackNumber <= 0 || trackNumber > queue.size()) {
            return "Track number out of range.";
        }
        for (int i = 1; i < trackNumber; i++) {
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

        if (this.loop) {
            this.loop = false;
            return "Loop is off!";
        }

        this.loop = true;
        return "Loop is on!";
    }

    public String shuffleQueue() {
        if (this.queue.isEmpty() || this.player.getPlayingTrack() == null) {
            this.shuffle = false;
            return "The queue is empty.";
        }

        if (this.shuffle) {
            this.queue.clear();

            Queue<AudioTrack> tempQueue = new LinkedList<>();

            while (!this.originalQueue.isEmpty() && this.originalQueue.peek() != this.player.getPlayingTrack()) {
                tempQueue.add(this.originalQueue.poll());
            }

            this.originalQueue.remove(this.player.getPlayingTrack());

            this.originalQueue.addAll(tempQueue);

            this.queue.addAll(this.originalQueue);

            this.shuffle = false;
            return "Shuffle is off!";
        }

        this.originalQueue.clear();
        this.originalQueue.addAll(this.queue);

        List<AudioTrack> tempList = new ArrayList<>();

        this.queue.drainTo(tempList);
        Collections.shuffle(tempList);
        this.queue.addAll(tempList);

        this.shuffle = true;
        return "Shuffle is on!";
    }

    public String jumpTo(String trackTime) {
        String[] parts = trackTime.split(":");

        if (parts.length < 2 || parts.length > 3) {
            return "Check the input format.";
        }

        int minutes = Integer.parseInt(parts[0]);
        int seconds = Integer.parseInt(parts[1]);
        int hours = 0;

        if (parts.length == 3) {
            hours = Integer.parseInt(parts[0]);
            minutes = Integer.parseInt(parts[1]);
            seconds = Integer.parseInt(parts[2]);
        }

        long jumpTimeMillis = (hours * 3600L + minutes * 60L + seconds) * 1000;

        if (jumpTimeMillis > this.player.getPlayingTrack().getDuration()) {
            return "Time longer than track's duration.";
        }
        Duration duration = Duration.ofMillis(jumpTimeMillis);

        this.player.getPlayingTrack().setPosition(jumpTimeMillis);

        String message = duration.toMinutes() + ":" + duration.toSeconds();
        if (duration.toHours() > 0) {
            message = duration.toHours() + ":" + duration.toMinutes() + ":" + duration.toSeconds();
        }
        return "Jumped to: " + message;
    }

    public String remove(int trackNumber) {
        if (this.player.getPlayingTrack() == null) {
            return "Nothing is being played right now.";
        }
        if (trackNumber <= 0 || trackNumber > queue.size()) {
            return "Track number out of range.";
        }

        Iterator<AudioTrack> iterator = queue.iterator();
        AudioTrack removedTrack = null;

        int currentIndex = 1;
        while (iterator.hasNext()) {
            AudioTrack currentTrack = iterator.next();
            if (currentIndex == trackNumber) {
                removedTrack = currentTrack;
                iterator.remove();
                break;
            }
            currentIndex++;
        }

        if (removedTrack == null) {
            return "An error occurred.";
        }

        return "Removed: " + getNowPlayingMessage(removedTrack, true);
    }

    public String autoplay() {
        if (!autoplay) {
            autoplay = true;
            return "Autoplay is on!";
        }
        autoplay = false;
        return "Autoplay is off!";
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
        message.append("Queue\n\n Now playing - [")
                .append(firstTrack.getInfo().title)
                .append("](")
                .append(firstTrack.getInfo().uri)
                .append(") - `")
                .append(formatDuration(firstTrack.getDuration()))
                .append("`\n\n Next tracks: \n");

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

        message.append("and `")
                .append(queue.size() - (trackNumber - 1))
                .append("` more.");

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

    public String getCurrentSongUrl() {
        return this.currentSongUrl;
    }
}
