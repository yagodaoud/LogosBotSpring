package yagodaoud.com.logos.music.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yagodaoud.com.logos.commands.CommandHandlerInterface;
import yagodaoud.com.logos.commands.CommandRegistryService;
import yagodaoud.com.logos.helper.Colors;
import yagodaoud.com.logos.music.audio.PlayerManager;

import java.util.List;

import static yagodaoud.com.logos.helper.MessageEmbedBuilder.messageEmbedBuilder;

@Component
public class SkipCommand implements CommandHandlerInterface {

    @Autowired
    public SkipCommand(CommandRegistryService commandRegistry) {
        commandRegistry.registerCommand(this);
    }

    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        if (PlayerManager.getInstance() == null) {
            event.replyEmbeds(messageEmbedBuilder("You must start the player first", Colors.ADVERT)).queue();
            return;
        }
        event.replyEmbeds(PlayerManager.getInstance().skipTrack(event.getGuild(), event.getMember().getVoiceState())).queue();
    }

    @Override
    public String getName() {
        return "skip";
    }

    @Override
    public String getDescription() {
        return "Skip to next track.";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }
}
