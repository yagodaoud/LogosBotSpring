package yagodaoud.com.logos.music.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yagodaoud.com.logos.commands.CommandRegistryService;
import yagodaoud.com.logos.commands.MusicCommandInterface;
import yagodaoud.com.logos.music.audio.PlayerManager;

import java.util.List;

@Component
public class SkipToCommand implements MusicCommandInterface {

    private final PlayerManager playerManager;

    @Autowired
    public SkipToCommand(CommandRegistryService commandRegistry, PlayerManager playerManager) {
        commandRegistry.registerCommand(this);
        this.playerManager = playerManager;
    }

    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        int trackNumber = event.getOption("track-number").getAsInt();
        event.replyEmbeds(playerManager.skipTo(event.getGuild(), event.getMember().getVoiceState(), trackNumber)).queue();
    }

    @Override
    public String getName() {
        return "skip-to";
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
