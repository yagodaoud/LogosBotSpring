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

import static yagodaoud.com.logos.music.commands.helper.PlayerNotStartedEmbedMessageBuilder.getPlayerNotStartedEmbedMessage;

@Component
public class JumpToCommand implements CommandHandlerInterface {

    @Autowired
    public JumpToCommand(CommandRegistryService commandRegistry) {
        commandRegistry.registerCommand(this);
    }

    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        if (PlayerManager.getInstance() == null) {
            event.replyEmbeds(getPlayerNotStartedEmbedMessage()).queue();
            return;
        }
        int trackNumber = event.getOption("track-number").getAsInt();
        event.replyEmbeds(PlayerManager.getInstance().jumpTo(event.getGuild(), event.getMember().getVoiceState(), trackNumber)).queue();
    }

    @Override
    public String getName() {
        return "jump-to";
    }

    @Override
    public String getDescription() {
        return "Skip to a certain track in the queue.";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(new OptionData(OptionType.STRING, "track-number", "Track number to skip to", true));
    }
}
