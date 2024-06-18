package yagodaoud.com.logos.music.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class GuildMusicManager {
    private static final Map<Guild, GuildMusicManager> guilds = new ConcurrentHashMap<>();
    private final Guild guild;
    public final AudioPlayer player;
    public final TrackScheduler scheduler;
    private final AudioPlayerSendHandler sendHandler;

    private GuildMusicManager(Guild guild, AudioPlayerManager playerManager) {
        this.guild = guild;
        this.player = playerManager.createPlayer();
        this.scheduler = new TrackScheduler(player, this);
        this.player.addListener(scheduler);
        this.sendHandler = new AudioPlayerSendHandler(player);
        this.guild.getAudioManager().setSendingHandler(sendHandler);
    }

    public static GuildMusicManager getOrCreateInstance(Guild guild, AudioPlayerManager playerManager) {
        return guilds.computeIfAbsent(guild, g -> new GuildMusicManager(guild, playerManager));
    }

    public static String restartPlayer(Guild guild, GuildVoiceState guildVoiceState, AudioPlayerManager playerManager) {
        GuildMusicManager guildMusicManager = guilds.remove(guild);
        if (guildMusicManager == null) {
            return "No player active right now.";
        }
        guildMusicManager.destroy();
        getOrCreateInstance(guild, playerManager);

        try {
            Thread.sleep(100); //Discord Api Delay
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        guildVoiceState.getGuild().getAudioManager().openAudioConnection(guildVoiceState.getChannel());
        return "Player restarted successfully";
    }

    public void destroy() {
        this.guild.getAudioManager().closeAudioConnection();
        this.player.destroy();
    }
}
