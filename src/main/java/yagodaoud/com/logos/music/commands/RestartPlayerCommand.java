package yagodaoud.com.logos.music.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yagodaoud.com.logos.commands.CommandRegistryService;
import yagodaoud.com.logos.commands.MusicCommandInterface;
import yagodaoud.com.logos.music.audio.PlayerManager;

import java.util.List;

import static yagodaoud.com.logos.tools.EmbedErrorMessageBuilder.getNotAdminEmbedMessage;

@Component
public class RestartPlayerCommand implements MusicCommandInterface {
    private final PlayerManager playerManager;

    @Autowired
    public RestartPlayerCommand(CommandRegistryService commandRegistryService, PlayerManager playerManager) {
        commandRegistryService.registerCommand(this);
        this.playerManager = playerManager;
    }
    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        if (!event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
            event.replyEmbeds(getNotAdminEmbedMessage()).queue();
            return;
        }
        event.replyEmbeds(playerManager.restartPlayer(event.getGuild(), event.getMember().getVoiceState())).queue();
    }

    @Override
    public String getName() {
        return "restart-player";
    }

    @Override
    public String getDescription() {
        return "Restart the player if stuck. Use it wisely.";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of();
    }
}
