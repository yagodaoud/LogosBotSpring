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

import java.util.HashMap;
import java.util.Map;

public class PlayerManager {

    private static PlayerManager INSTANCE;

    private final Map<Long, AudioManager> musicManagers = new HashMap<>();
    private final AudioPlayerManager audioPlayerManager = new DefaultAudioPlayerManager();
    private final AudioManager audioManager = AudioManager.getInstance(this.audioPlayerManager);


    public PlayerManager() {
        AudioSourceManagers.registerRemoteSources(this.audioPlayerManager);
        AudioSourceManagers.registerLocalSource(this.audioPlayerManager);
    }

    public String loadAndPlay(TextChannel channel, GuildVoiceState guildVoiceState, String urlOrName) {
        if (!guildVoiceState.inAudioChannel()) {
            return "You must be in a voice channel first.";
        }
        final AudioManager musicManager = this.getMusicManager(channel.getGuild());

        AudioEventHandler audioEventHandler = new AudioEventHandler(guildVoiceState);
        audioEventHandler.joinVoiceChannel();
        urlOrName = "ytsearch:" + urlOrName;
        this.audioPlayerManager.loadItemOrdered(musicManager, urlOrName, new AudioLoadResultHandler() {
            @Override
            public void trackLoaded(AudioTrack track) {
            }

            @Override
            public void playlistLoaded(AudioPlaylist playlist) {
                audioManager.scheduler.queue(playlist.getTracks().get(0));
            }

            @Override
            public void noMatches() {
                System.out.println("c");
            }

            @Override
            public void loadFailed(FriendlyException exception) {
                System.out.println("d");

            }
        });
        return "a";
    }

    public AudioManager getMusicManager(Guild guild) {
        return this.musicManagers.computeIfAbsent(guild.getIdLong(), (guildId) -> {
            guild.getAudioManager().setSendingHandler(audioManager.getSendHandler());
            return audioManager;
        });
    }

    public static PlayerManager getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new PlayerManager();
        }
        return INSTANCE;
    }
}
