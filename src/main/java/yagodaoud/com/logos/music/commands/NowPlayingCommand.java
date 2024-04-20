package yagodaoud.com.logos.music.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yagodaoud.com.logos.commands.CommandHandlerInterface;
import yagodaoud.com.logos.commands.CommandRegistryService;
import yagodaoud.com.logos.music.audio.PlayerManager;

import java.util.List;

import static yagodaoud.com.logos.tools.EmbedErrorMessageBuilder.getPlayerNotStartedEmbedMessage;

@Component
public class NowPlayingCommand implements CommandHandlerInterface {

    @Autowired
    public NowPlayingCommand(CommandRegistryService commandRegistry) {
        commandRegistry.registerCommand(this);
    }

    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        if (PlayerManager.getInstance() == null) {
            event.replyEmbeds(getPlayerNotStartedEmbedMessage()).queue();
            return;
        }
        event.replyEmbeds(PlayerManager.getInstance().nowPlaying(event.getGuild(), event.getMember().getVoiceState())).queue();
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
