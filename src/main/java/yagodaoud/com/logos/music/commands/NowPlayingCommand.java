package yagodaoud.com.logos.music.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yagodaoud.com.logos.commands.CommandRegistryService;
import yagodaoud.com.logos.commands.MusicCommandInterface;
import yagodaoud.com.logos.music.audio.PlayerManager;

import java.util.List;

@Component
public class NowPlayingCommand implements MusicCommandInterface {

    private final PlayerManager playerManager;

    @Autowired
    public NowPlayingCommand(CommandRegistryService commandRegistry, PlayerManager playerManager) {
        commandRegistry.registerCommand(this);
        this.playerManager = playerManager;
    }

    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        event.replyEmbeds(playerManager.nowPlaying(event.getGuild(), event.getMember().getVoiceState())).queue();
    }

    @Override
    public String getName() {
        return "now-playing";
    }

    @Override
    public String getDescription() {
        return "Show the current track being played.";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }
}
