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

import static yagodaoud.com.logos.tools.EmbedErrorMessageBuilder.getNotAdminEmbedMessage;
import static yagodaoud.com.logos.tools.EmbedErrorMessageBuilder.getWrongOptionTypeMessage;

@Component
public class VolumeCommand implements CommandHandlerInterface {

    private final PlayerManager playerManager;

    @Autowired
    public VolumeCommand(CommandRegistryService commandRegistry, PlayerManager playerManager) {
        commandRegistry.registerCommand(this);
        this.playerManager = playerManager;
    }

    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        if (!event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
            event.replyEmbeds(getNotAdminEmbedMessage()).queue();
            return;
        }

        int volume;
        try {
            volume = event.getOption("volume").getAsInt();
        } catch (Exception e) {
            event.replyEmbeds(getWrongOptionTypeMessage("number")).queue();
            return;
        }
        event.replyEmbeds(playerManager.setVolume(event.getGuild(), event.getMember().getVoiceState(), volume)).queue();
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
