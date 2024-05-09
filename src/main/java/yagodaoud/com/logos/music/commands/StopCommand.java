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
public class StopCommand implements CommandHandlerInterface {

    private final PlayerManager playerManager;

    @Autowired
    public StopCommand(CommandRegistryService commandRegistry, PlayerManager playerManager) {
        commandRegistry.registerCommand(this);
        this.playerManager = playerManager;
    }
    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        event.replyEmbeds(playerManager.stopPlayer(event.getGuild(), event.getMember().getVoiceState())).queue();
    }

    @Override
    public String getName() {
        return "stop";
    }

    @Override
    public String getDescription() {
        return "Stop the player.";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }
}
