package yagodaoud.com.logos.music.audio;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayer;
import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.entities.Guild;

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
        this.scheduler = new TrackScheduler(player);
        this.player.addListener(scheduler);
        this.sendHandler = new AudioPlayerSendHandler(player);
        this.guild.getAudioManager().setSendingHandler(sendHandler);
    }

    public static GuildMusicManager getOrCreateInstance(Guild guild, AudioPlayerManager playerManager) {
        return guilds.computeIfAbsent(guild, g -> new GuildMusicManager(guild, playerManager));
    }
}
