package yagodaoud.com.logos.music.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yagodaoud.com.logos.commands.CommandHandlerInterface;
import yagodaoud.com.logos.commands.CommandRegistryService;
import yagodaoud.com.logos.music.audio.PlayerManager;

import java.util.List;

import static yagodaoud.com.logos.music.commands.helper.PlayerNotStartedEmbedMessageBuilder.getPlayerNotStartedEmbedMessage;

@Component
public class ShuffleCommand implements CommandHandlerInterface {

    @Autowired
    public ShuffleCommand(CommandRegistryService commandRegistryService) {
        commandRegistryService.registerCommand(this);
    }
    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        if (PlayerManager.getInstance() == null) {
            event.replyEmbeds(getPlayerNotStartedEmbedMessage()).queue();
            return;
        }
        event.replyEmbeds(PlayerManager.getInstance().shuffleQueue(event.getGuild(), event.getMember().getVoiceState())).queue();
    }

    @Override
    public String getName() {
        return "shuffle";
    }

    @Override
    public String getDescription() {
        return "Shuffle the queue.";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }
}
