package yagodaoud.com.logos.music.commands;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yagodaoud.com.logos.commands.CommandHandlerInterface;
import yagodaoud.com.logos.commands.CommandRegistryService;
import yagodaoud.com.logos.music.audio.PlayerManager;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class PlayCommand implements CommandHandlerInterface {

    @Autowired
    public PlayCommand(CommandRegistryService commandRegistry) {
        commandRegistry.registerCommand(this);
    }

    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        PlayerManager playerManager = new PlayerManager();
        CompletableFuture<MessageEmbed> loadResultFuture = (playerManager.loadAndPlay(event.getChannel().asTextChannel(), event.getMember().getVoiceState(), event.getOption("query").getAsString(), false));
        loadResultFuture.thenAccept(result -> event.replyEmbeds(result).queue());
    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getDescription() {
        return "Play a song or playlist from YouTube.";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(new OptionData(OptionType.STRING, "query", "Song name/playlist/link", true));
    }
}
