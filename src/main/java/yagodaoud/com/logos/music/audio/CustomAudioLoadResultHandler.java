package yagodaoud.com.logos.music.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;
import yagodaoud.com.logos.music.audio.conversion.spotify.SpotifyAudioObject;
import yagodaoud.com.logos.tools.Colors;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import static yagodaoud.com.logos.tools.MessageEmbedBuilder.messageEmbedBuilder;

public class CustomAudioLoadResultHandler implements AudioLoadResultHandler {
    private final GuildMusicManager musicManager;
    private final String finalUrlOrName;
    private final SpotifyAudioObject spotifyAudioObject;
    private final boolean forcePlay;
    private final CompletableFuture<MessageEmbed> futureMessage;
    private final AtomicReference<MessageEmbed> messageContainer;

    public CustomAudioLoadResultHandler(GuildMusicManager musicManager, String finalUrlOrName, boolean forcePlay, CompletableFuture<MessageEmbed> futureMessage, AtomicReference<MessageEmbed> messageContainer) {
        this.musicManager = musicManager;
        this.finalUrlOrName = finalUrlOrName;
        this.spotifyAudioObject = null;
        this.forcePlay = forcePlay;
        this.futureMessage = futureMessage;
        this.messageContainer = messageContainer;
    }

    public CustomAudioLoadResultHandler(GuildMusicManager musicManager, SpotifyAudioObject spotifyAudioObject, boolean forcePlay, CompletableFuture<MessageEmbed> futureMessage, AtomicReference<MessageEmbed> messageContainer) {
        this.musicManager = musicManager;
        this.spotifyAudioObject = spotifyAudioObject;
        this.finalUrlOrName = null;
        this.forcePlay = forcePlay;
        this.futureMessage = futureMessage;
        this.messageContainer = messageContainer;
    }

    @Override
    public void trackLoaded(AudioTrack track) {
        musicManager.scheduler.queue(track, forcePlay);
        completeFutureWithMessage(futureMessage, messageContainer, songMessageBuilder(track, 0));
    }

    @Override
    public void playlistLoaded(AudioPlaylist playlist) {
        if (playlist.getTracks().isEmpty()) {
            noMatches();
            return;
        }
        AudioTrack firstTrack = playlist.getTracks().remove(0);
        musicManager.scheduler.queue(firstTrack, forcePlay);

        if (finalUrlOrName != null && (finalUrlOrName.contains("/playlist") || finalUrlOrName.contains("/sets")) && !forcePlay) {
            for (AudioTrack track : playlist.getTracks()) {
                musicManager.scheduler.queue(track, forcePlay);
            }
            completeFutureWithMessage(futureMessage, messageContainer, songMessageBuilder(firstTrack, playlist.getTracks().size()));
        }
        completeFutureWithMessage(futureMessage, messageContainer, songMessageBuilder(firstTrack, 0));
    }

    @Override
    public void noMatches() {
        completeFutureWithMessage(futureMessage, messageContainer, messageEmbedBuilder("No matches found.", Colors.ADVERT));
    }

    @Override
    public void loadFailed(FriendlyException exception) {
        completeFutureWithMessage(futureMessage, messageContainer, messageEmbedBuilder("Failed to load: " + exception.getMessage(), Colors.ADVERT));
    }

    private void completeFutureWithMessage(CompletableFuture<MessageEmbed> future, AtomicReference<MessageEmbed> messageContainer, MessageEmbed message) {
        messageContainer.set(message);
        future.complete(messageContainer.get());
    }

    public MessageEmbed songMessageBuilder(AudioTrack track, int size) {
        EmbedBuilder embedBuilder = new EmbedBuilder();

        String message = "Added: [" +
                track.getInfo().title +
                "](" + track.getInfo().uri + ")  -  `" +
                formatDuration(track.getDuration()) +
                "`";

        if (size > 0) {
            message += " and `" +
                    size +
                    "` more";
        }

        embedBuilder.setDescription(message);
        embedBuilder.setColor(Colors.SONG_OR_PLAYLIST_ADDED);

        return embedBuilder.build();
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

    public CompletableFuture<MessageEmbed> getFutureMessage() {
        return this.futureMessage;
    }
}
