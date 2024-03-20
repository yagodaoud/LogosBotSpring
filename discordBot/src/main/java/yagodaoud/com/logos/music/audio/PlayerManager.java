package yagodaoud.com.logos.music.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class PlayerManager {

    private static PlayerManager INSTANCE;
    private final Map<Long, AudioManager> musicManagers = new HashMap<>();
    private final AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();
    private final AudioManager audioManager = AudioManager.getInstance(this.audioPlayerManager);

    public PlayerManager() {
        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public CompletableFuture<String> loadAndPlay(TextChannel channel, GuildVoiceState guildVoiceState, String urlOrName) {
        CompletableFuture<String> futureMessage = new CompletableFuture<>();
        final AtomicReference<String> messageContainer = new AtomicReference<>();

        if (!guildVoiceState.inAudioChannel()) {
            completeFutureWithMessage(futureMessage, messageContainer, "You must be in a voice channel first.");
            return futureMessage;
        }

        final AudioManager musicManager = this.getMusicManager(channel.getGuild());

        AudioEventHandler audioEventHandler = new AudioEventHandler(guildVoiceState);
        audioEventHandler.joinVoiceChannel();

        if (urlOrName == null) {
            completeFutureWithMessage(futureMessage, messageContainer, "Something went wrong.");
            return futureMessage;
        }

         if (!isUrl(urlOrName)) {
             urlOrName = "ytsearch:" + urlOrName;
         }

        String finalUrlOrName = urlOrName;
        this.audioPlayerManager.loadItemOrdered(musicManager, finalUrlOrName, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                audioManager.scheduler.queue(track);
                completeFutureWithMessage(futureMessage, messageContainer, this.songMessageBuilder(track, 0));
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
    //                playlist.getTracks().forEach(System.out::println);

                AudioTrack firstTrack = playlist.getTracks().remove(0);
                audioManager.scheduler.queue(firstTrack);


                if (finalUrlOrName.contains("/playlist")) {
                    for (AudioTrack track : playlist.getTracks()) {
                        audioManager.scheduler.queue(track);
                    }
                    completeFutureWithMessage(futureMessage, messageContainer, this.songMessageBuilder(firstTrack, playlist.getTracks().size()));
                }

                completeFutureWithMessage(futureMessage, messageContainer, this.songMessageBuilder(firstTrack, 0));

            }

            private String songMessageBuilder(AudioTrack track, int size) {

                String message = "Added to queue: `" +
                        track.getInfo().title +
                        " (" +
                        formatDuration(track.getDuration()) +
                        ")` by `" +
                        track.getInfo().author +
                        "`";

                if (size > 0) {
                    message +=  " and `" +
                                size +
                                "` more";
                }
                return message;
            }

            @Override
            public void noMatches() {
                completeFutureWithMessage(futureMessage, messageContainer, "No matches found.");
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                completeFutureWithMessage(futureMessage, messageContainer, "Failed to load: " + exception.getMessage());
            }
        });
        return futureMessage;
    }

    public String skipTrack(AudioManager audioManager) {
        return audioManager.scheduler.nextTrack();
    }

    public AudioManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            guild.getAudioManager().setSendingHandler(audioManager.getSendHandler());
            return audioManager;
        });
    }

    private boolean isUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (Exception e){
            return false;
        }
    }

    private void completeFutureWithMessage(CompletableFuture<String> future, AtomicReference<String> messageContainer, String message) {
        messageContainer.set(message);
        future.complete(messageContainer.get());
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

    public static PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }
}
