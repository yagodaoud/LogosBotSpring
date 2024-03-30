package yagodaoud.com.logos.music.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioLoadResultHandler;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.player.DefaultAudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import com.sedmelluq.discord.lavaplayer.tools.FriendlyException;
import com.sedmelluq.discord.lavaplayer.track.AudioPlaylist;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import yagodaoud.com.logos.helper.Colors;
import yagodaoud.com.logos.music.services.VolumeService;

import java.net.URL;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import static yagodaoud.com.logos.helper.MessageEmbedBuilder.messageEmbedBuilder;

public class PlayerManager {
    private final AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();
    private static PlayerManager INSTANCE;

    public PlayerManager() {
        AudioSourceManagers.registerRemoteSources(audioPlayerManager);
        AudioSourceManagers.registerLocalSource(audioPlayerManager);

        INSTANCE = this;
    }

    public CompletableFuture<MessageEmbed> loadAndPlay(TextChannel channel, GuildVoiceState guildVoiceState, String urlOrName) {
        CompletableFuture<MessageEmbed> futureMessage = new CompletableFuture<>();
        final AtomicReference<MessageEmbed> messageContainer = new AtomicReference<>();

        if (!guildVoiceState.inAudioChannel()) {
            completeFutureWithMessage(futureMessage, messageContainer, messageEmbedBuilder("You must be in a voice channel first.", Colors.ADVERT));
            return futureMessage;
        }

        final GuildMusicManager musicManager = GuildMusicManager.getOrCreateInstance(channel.getGuild(), this.audioPlayerManager);

        AudioEventHandler audioEventHandler = new AudioEventHandler(guildVoiceState);

        audioEventHandler.joinVoiceChannel();

        if (urlOrName == null) {
            completeFutureWithMessage(futureMessage, messageContainer,  messageEmbedBuilder("Something went wrong.", Colors.ADVERT));
            return futureMessage;
        }

         if (!isUrl(urlOrName)) {
             urlOrName = "ytsearch:" + urlOrName;
         }

        String finalUrlOrName = urlOrName;

        AudioPlayerManager audioPlayerManager = this.audioPlayerManager;

        audioPlayerManager.loadItemOrdered(musicManager, finalUrlOrName, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
                musicManager.scheduler.queue(track);
                completeFutureWithMessage(futureMessage, messageContainer, songMessageBuilder(track, 0));
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
    //                playlist.getTracks().forEach(System.out::println);

                AudioTrack firstTrack = playlist.getTracks().remove(0);
                musicManager.scheduler.queue(firstTrack);

                if (finalUrlOrName.contains("/playlist")) {
                    for (AudioTrack track : playlist.getTracks()) {
                        musicManager.scheduler.queue(track);
                    }
                    completeFutureWithMessage(futureMessage, messageContainer, songMessageBuilder(firstTrack, playlist.getTracks().size()));
                }
                completeFutureWithMessage(futureMessage, messageContainer, songMessageBuilder(firstTrack, 0));
            }

            @Override
            public void noMatches() {
                completeFutureWithMessage(futureMessage, messageContainer,  messageEmbedBuilder("No matches found.", Colors.ADVERT));
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                completeFutureWithMessage(futureMessage, messageContainer, messageEmbedBuilder("Failed to load: " + exception.getMessage(), Colors.ADVERT));
            }
        });
        return futureMessage;
    }

    public MessageEmbed skipTrack(Guild guild, GuildVoiceState voiceState) {
        if (!voiceState.inAudioChannel()) {
            return messageEmbedBuilder("You must be in a voice channel first.", Colors.ADVERT);
        }
        GuildMusicManager musicManager = GuildMusicManager.getOrCreateInstance(guild, this.audioPlayerManager);
        return messageEmbedBuilder(musicManager.scheduler.nextTrack(), Colors.SUCCESS);
    }

    public MessageEmbed nowPlaying(Guild guild, GuildVoiceState voiceState) {
        if (!voiceState.inAudioChannel()) {
            return messageEmbedBuilder("You must be in a voice channel first.", Colors.ADVERT);
        }
        GuildMusicManager musicManager = GuildMusicManager.getOrCreateInstance(guild, this.audioPlayerManager);
        return messageEmbedBuilder(musicManager.scheduler.nowPlaying(), Colors.QUEUE_OR_NOW_PLAYING);
    }

    public MessageEmbed getQueue(Guild guild, GuildVoiceState voiceState) {
        if (!voiceState.inAudioChannel()) {
            return messageEmbedBuilder("You must be in a voice channel first.", Colors.ADVERT);
        }
        GuildMusicManager musicManager = GuildMusicManager.getOrCreateInstance(guild, this.audioPlayerManager);
        return messageEmbedBuilder(musicManager.scheduler.getQueue(), Colors.QUEUE_OR_NOW_PLAYING);
    }

    public MessageEmbed setVolume(Guild guild, GuildVoiceState voiceState, int volume) {
        if (!voiceState.inAudioChannel()) {
            return messageEmbedBuilder("You must be in a voice channel first.", Colors.ADVERT);
        }
        GuildMusicManager musicManager = GuildMusicManager.getOrCreateInstance(guild, this.audioPlayerManager);
        return messageEmbedBuilder(VolumeService.setVolume(musicManager.player, volume), Colors.SUCCESS);
    }

    public MessageEmbed stopPlayer(Guild guild, GuildVoiceState voiceState) {
        if (!voiceState.inAudioChannel()) {
            return messageEmbedBuilder("You must be in a voice channel first.", Colors.ADVERT);
        }
        GuildMusicManager musicManager = GuildMusicManager.getOrCreateInstance(guild, this.audioPlayerManager);
        return messageEmbedBuilder(musicManager.scheduler.stopPlayer(), Colors.SUCCESS);
    }

    public MessageEmbed resumePlayer(Guild guild, GuildVoiceState voiceState) {
        if (!voiceState.inAudioChannel()) {
            return messageEmbedBuilder("You must be in a voice channel first.", Colors.ADVERT);
        }
        GuildMusicManager musicManager = GuildMusicManager.getOrCreateInstance(guild, this.audioPlayerManager);
        return messageEmbedBuilder(musicManager.scheduler.resumePlayer(), Colors.SUCCESS);
    }

    public MessageEmbed jumpTo(Guild guild, GuildVoiceState voiceState, int trackNumber) {
        if (!voiceState.inAudioChannel()) {
            return messageEmbedBuilder("You must be in a voice channel first.", Colors.ADVERT);
        }
        GuildMusicManager musicManager = GuildMusicManager.getOrCreateInstance(guild, this.audioPlayerManager);
        return messageEmbedBuilder(musicManager.scheduler.jumpTo(trackNumber), Colors.SUCCESS);
    }

    private boolean isUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (Exception e){
            return false;
        }
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
            message +=  " and `" +
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

    public static PlayerManager getInstance() {
        return INSTANCE;
    }

}
