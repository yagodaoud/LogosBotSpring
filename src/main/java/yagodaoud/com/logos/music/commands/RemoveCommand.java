package yagodaoud.com.logos.music.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yagodaoud.com.logos.commands.CommandHandlerInterface;
import yagodaoud.com.logos.commands.CommandRegistryService;
import yagodaoud.com.logos.music.audio.PlayerManager;

import java.util.List;

import static yagodaoud.com.logos.tools.EmbedErrorMessageBuilder.getPlayerNotStartedEmbedMessage;
import static yagodaoud.com.logos.tools.EmbedErrorMessageBuilder.getWrongOptionTypeMessage;

@Component
public class RemoveCommand implements CommandHandlerInterface {

    @Autowired
    public RemoveCommand(CommandRegistryService commandRegistry) {
        commandRegistry.registerCommand(this);
    }

    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        if (PlayerManager.getInstance() == null) {
            event.replyEmbeds(getPlayerNotStartedEmbedMessage()).queue();
            return;
        }
        if (event.getOption("track-index").getType() != OptionType.INTEGER) {
            event.replyEmbeds(getWrongOptionTypeMessage("number")).queue();
            return;
        }
        int trackNumber = event.getOption("track-index").getAsInt();
        event.replyEmbeds(PlayerManager.getInstance().remove(event.getGuild(), event.getMember().getVoiceState(), trackNumber)).queue();
    }

    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return "Remove the given track from the queue.";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(new OptionData(OptionType.STRING, "track-index", "Track number to be removed", true));
    }
}
