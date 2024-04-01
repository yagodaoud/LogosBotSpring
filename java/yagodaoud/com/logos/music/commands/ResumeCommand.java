package yagodaoud.com.logos.music.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yagodaoud.com.logos.commands.CommandHandlerInterface;
import yagodaoud.com.logos.commands.CommandRegistryService;
import yagodaoud.com.logos.music.audio.PlayerManager;

import java.util.List;

import static yagodaoud.com.logos.music.commands.helper.EmbedErrorMessageBuilder.getPlayerNotStartedEmbedMessage;

@Component
public class ResumeCommand implements CommandHandlerInterface {

    @Autowired
    public ResumeCommand(CommandRegistryService commandRegistry) {
        commandRegistry.registerCommand(this);
    }
    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        if (PlayerManager.getInstance() == null) {
            event.replyEmbeds(getPlayerNotStartedEmbedMessage()).queue();
            return;
        }
        event.replyEmbeds(PlayerManager.getInstance().resumePlayer(event.getGuild(), event.getMember().getVoiceState())).queue();
    }

    @Override
    public String getName() {
        return "resume";
    }

    @Override
    public String getDescription() {
        return "Resume the player.";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }
}
