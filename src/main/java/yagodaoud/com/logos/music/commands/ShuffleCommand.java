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
public class ShuffleCommand implements MusicCommandInterface {

    private final PlayerManager playerManager;

    @Autowired
    public ShuffleCommand(CommandRegistryService commandRegistryService, PlayerManager playerManager) {
        commandRegistryService.registerCommand(this);
        this.playerManager = playerManager;
    }
    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        event.replyEmbeds(playerManager.shuffleQueue(event.getGuild(), event.getMember().getVoiceState())).queue();
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
