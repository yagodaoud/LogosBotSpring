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

@Component
public class JumpToCommand implements CommandHandlerInterface {

    private final PlayerManager playerManager;

    @Autowired
    public JumpToCommand(CommandRegistryService commandRegistry, PlayerManager playerManager) {
        commandRegistry.registerCommand(this);
        this.playerManager = playerManager;
    }

    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        String trackNumber = event.getOption("track-time").getAsString();
        event.replyEmbeds(playerManager.jumpTo(event.getGuild(), event.getMember().getVoiceState(), trackNumber)).queue();
    }

    @Override
    public String getName() {
        return "jump-to";
    }

    @Override
    public String getDescription() {
        return "Jump to a specific time in the track.";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(new OptionData(OptionType.STRING, "track-time", "Time to jump to (mm:ss or hh:mm:ss)", true));
    }
}

