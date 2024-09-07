package yagodaoud.com.logos.music.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.source.AudioSourceManagers;
import dev.lavalink.youtube.YoutubeAudioSourceManager;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import yagodaoud.com.logos.music.audio.conversion.spotify.SpotifyAudioObject;
import yagodaoud.com.logos.music.audio.conversion.spotify.SpotifyHandler;
import yagodaoud.com.logos.music.audio.conversion.spotify.services.SpotifyApiConnection;
import yagodaoud.com.logos.music.audio.conversion.spotify.services.SpotifyApiService;
import yagodaoud.com.logos.music.audio.conversion.youtube.YoutubeScraper;
import yagodaoud.com.logos.music.services.VolumeService;
import yagodaoud.com.logos.tools.Colors;

import java.net.URL;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import static yagodaoud.com.logos.tools.MessageEmbedBuilder.messageEmbedBuilder;

@Service
public class PlayerManager {
    private final AudioPlayerManager audioPlayerManager;
    private static AudioPlayerManager audioPlayerManagerInstance;
    private SpotifyHandler spotifyHandler;
    private final ApplicationEventPublisher eventPublisher;

    @Autowired
    public PlayerManager(ApplicationEventPublisher eventPublisher, AudioPlayerManager audioPlayerManager) {
        this.eventPublisher = eventPublisher;
        this.audioPlayerManager = audioPlayerManager;
        audioPlayerManagerInstance = audioPlayerManager;
        YoutubeAudioSourceManager ytSourceManager = new YoutubeAudioSourceManager();
        audioPlayerManager.registerSourceManager(ytSourceManager);
        AudioSourceManagers.registerLocalSource(audioPlayerManager);
    }

    @SneakyThrows
    public CompletableFuture<MessageEmbed> loadAndPlay(TextChannel channel, GuildVoiceState guildVoiceState, String urlOrName, String provider, boolean forcePlay) {
        CompletableFuture<MessageEmbed> futureMessage = new CompletableFuture<>();
        final AtomicReference<MessageEmbed> messageContainer = new AtomicReference<>();

        if (!guildVoiceState.inAudioChannel()) {
            completeFutureWithMessage(futureMessage, messageContainer, messageEmbedBuilder("You must be in a voice channel first.", Colors.ADVERT));
            return futureMessage;
        }

        final GuildMusicManager musicManager = GuildMusicManager.getOrCreateInstance(channel.getGuild(), this.audioPlayerManager);

        AudioEventHandler audioEventHandler = new AudioEventHandler(eventPublisher, guildVoiceState);

        audioEventHandler.joinVoiceChannel();

        if (urlOrName == null) {
            completeFutureWithMessage(futureMessage, messageContainer, messageEmbedBuilder("Something went wrong.", Colors.ADVERT));
            return futureMessage;
        }

        if (!isUrl(urlOrName)) {
            // ytsearch / scsearch

            String webProvider = "ytsearch:";

            if (provider.equals("sc")) {
                webProvider = "scsearch:";
            }

            urlOrName = webProvider + urlOrName;
        }

        if (urlOrName.contains("open.spotify")) {
            spotifyHandler = new SpotifyHandler(new SpotifyApiService(new SpotifyApiConnection(new RestTemplate())));
            SpotifyAudioObject spotifyAudioObject = spotifyHandler.getSpotifyObject(urlOrName);

            String response = spotifyHandler.handle(this.audioPlayerManager, musicManager, spotifyAudioObject, forcePlay, futureMessage, messageContainer);
            completeFutureWithMessage(futureMessage, messageContainer, messageEmbedBuilder(response, Colors.SONG_OR_PLAYLIST_ADDED));
            return futureMessage;
        }

        String finalUrlOrName = urlOrName;

        AudioPlayerManager audioPlayerManager = this.audioPlayerManager;
        CustomAudioLoadResultHandler loadResultHandlerImplementation = new CustomAudioLoadResultHandler(musicManager, finalUrlOrName, forcePlay, futureMessage, messageContainer);

        audioPlayerManager.loadItemOrdered(musicManager, finalUrlOrName, loadResultHandlerImplementation);

        futureMessage = loadResultHandlerImplementation.getFutureMessage();
        return futureMessage;
    }

    @SneakyThrows
    public static void loadAutoplayTrack(TrackScheduler trackScheduler, AudioPlayerManager audioPlayerManager, GuildMusicManager musicManager, CustomAudioLoadResultHandler loadResultHandlerImplementation) {
        Thread.sleep(200);
        String currentSongUrl = trackScheduler.getCurrentSongUrl();
            String finalUrlOrName = YoutubeScraper.getNextRecommendedVideoUrl(currentSongUrl);
            System.out.println(finalUrlOrName);
            audioPlayerManager.loadItemOrdered(musicManager, finalUrlOrName, loadResultHandlerImplementation);
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

    public MessageEmbed clearQueue(Guild guild, GuildVoiceState voiceState) {
        if (!voiceState.inAudioChannel()) {
            return messageEmbedBuilder("You must be in a voice channel first.", Colors.ADVERT);
        }
        GuildMusicManager musicManager = GuildMusicManager.getOrCreateInstance(guild, this.audioPlayerManager);
        return messageEmbedBuilder(musicManager.scheduler.clearQueue(), Colors.SUCCESS);
    }

    public MessageEmbed shuffleQueue(Guild guild, GuildVoiceState voiceState) {
        if (!voiceState.inAudioChannel()) {
            return messageEmbedBuilder("You must be in a voice channel first.", Colors.ADVERT);
        }
        GuildMusicManager musicManager = GuildMusicManager.getOrCreateInstance(guild, this.audioPlayerManager);
        return messageEmbedBuilder(musicManager.scheduler.shuffleQueue(), Colors.SUCCESS);
    }

    public MessageEmbed skipTo(Guild guild, GuildVoiceState voiceState, int trackNumber) {
        if (!voiceState.inAudioChannel()) {
            return messageEmbedBuilder("You must be in a voice channel first.", Colors.ADVERT);
        }
        GuildMusicManager musicManager = GuildMusicManager.getOrCreateInstance(guild, this.audioPlayerManager);
        return messageEmbedBuilder(musicManager.scheduler.skipTo(trackNumber), Colors.SUCCESS);
    }

    public CompletableFuture<MessageEmbed> forcePlay(TextChannel channel, GuildVoiceState voiceState, String urlOrName, String provider) {
        return this.loadAndPlay(channel, voiceState, urlOrName, provider, true);
    }

    public MessageEmbed loopQueue(Guild guild, GuildVoiceState voiceState) {
        if (!voiceState.inAudioChannel()) {
            return messageEmbedBuilder("You must be in a voice channel first.", Colors.ADVERT);
        }
        GuildMusicManager musicManager = GuildMusicManager.getOrCreateInstance(guild, this.audioPlayerManager);
        return messageEmbedBuilder(musicManager.scheduler.loopQueue(), Colors.SUCCESS);
    }

    public MessageEmbed jumpTo(Guild guild, GuildVoiceState voiceState, String trackTime) {
        if (!voiceState.inAudioChannel()) {
            return messageEmbedBuilder("You must be in a voice channel first.", Colors.ADVERT);
        }
        GuildMusicManager musicManager = GuildMusicManager.getOrCreateInstance(guild, this.audioPlayerManager);
        return messageEmbedBuilder(musicManager.scheduler.jumpTo(trackTime), Colors.SUCCESS);
    }

    public MessageEmbed remove(Guild guild, GuildVoiceState voiceState, int trackNumber) {
        if (!voiceState.inAudioChannel()) {
            return messageEmbedBuilder("You must be in a voice channel first.", Colors.ADVERT);
        }
        GuildMusicManager musicManager = GuildMusicManager.getOrCreateInstance(guild, this.audioPlayerManager);
        return messageEmbedBuilder(musicManager.scheduler.remove(trackNumber), Colors.SUCCESS);
    }

    public MessageEmbed restartPlayer(Guild guild, GuildVoiceState voiceState) {
        if (!voiceState.inAudioChannel()) {
            return messageEmbedBuilder("You must be in a voice channel first.", Colors.ADVERT);
        }
        return messageEmbedBuilder(GuildMusicManager.restartPlayer(guild, voiceState, this.audioPlayerManager), Colors.SUCCESS);
    }

    public MessageEmbed autoplay(Guild guild, GuildVoiceState voiceState) {
        if (!voiceState.inAudioChannel()) {
            return messageEmbedBuilder("You must be in a voice channel first.", Colors.ADVERT);
        }
        GuildMusicManager musicManager = GuildMusicManager.getOrCreateInstance(guild, this.audioPlayerManager);
        return messageEmbedBuilder(musicManager.scheduler.autoplay(), Colors.SUCCESS);
    }

    private boolean isUrl(String url) {
        try {
            new URL(url);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    private void completeFutureWithMessage(CompletableFuture<MessageEmbed> future, AtomicReference<MessageEmbed> messageContainer, MessageEmbed message) {
        messageContainer.set(message);
        future.complete(messageContainer.get());
    }

    public static AudioPlayerManager getAudioPlayerManager() {
        return audioPlayerManagerInstance;
    }
}
