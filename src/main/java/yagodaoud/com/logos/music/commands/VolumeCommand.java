package yagodaoud.com.logos.music.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yagodaoud.com.logos.commands.CommandHandlerInterface;
import yagodaoud.com.logos.commands.CommandRegistryService;
import yagodaoud.com.logos.music.audio.PlayerManager;

import java.util.List;

import static yagodaoud.com.logos.music.commands.helper.EmbedErrorMessageBuilder.*;

@Component
public class VolumeCommand implements CommandHandlerInterface {

    @Autowired
    public VolumeCommand(CommandRegistryService commandRegistry) {
        commandRegistry.registerCommand(this);
    }

    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        if (!event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
            event.replyEmbeds(getNotAdminEmbedMessage()).queue();
            return;
        }
        if (PlayerManager.getInstance() == null) {
            event.replyEmbeds(getPlayerNotStartedEmbedMessage()).queue();
            return;
        }
        if (event.getOption("volume").getType() != OptionType.INTEGER) {
            event.replyEmbeds(getWrongOptionTypeMessage("number")).queue();
            return;
        }
        int volume = event.getOption("volume").getAsInt();
        event.replyEmbeds(PlayerManager.getInstance().setVolume(event.getGuild(), event.getMember().getVoiceState(), volume)).queue();
    }

    @Override
    public String getName() {
        return "volume";
    }

    @Override
    public String getDescription() {
        return "Change the volume of the player.";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(new OptionData(OptionType.STRING, "volume", "Volume between 0 - 100", true));
    }
}
