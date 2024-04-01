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

import static yagodaoud.com.logos.music.commands.helper.EmbedErrorMessageBuilder.getPlayerNotStartedEmbedMessage;

@Component
public class ForcePlayCommand implements CommandHandlerInterface {

    @Autowired
    public ForcePlayCommand(CommandRegistryService commandRegistry) {
        commandRegistry.registerCommand(this);
    }

    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        if (PlayerManager.getInstance() == null) {
            event.replyEmbeds(getPlayerNotStartedEmbedMessage()).queue();
            return;
        }

        CompletableFuture<MessageEmbed> loadResultFuture = (PlayerManager.getInstance().forcePlay(event.getChannel().asTextChannel(), event.getMember().getVoiceState(), event.getOption("force-play-query").getAsString()));
        loadResultFuture.thenAccept(result -> event.replyEmbeds(result).queue());
    }

    @Override
    public String getName() {
        return "force-play";
    }

    @Override
    public String getDescription() {
        return "Play a song immediately, regardless the queue.";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(new OptionData(OptionType.STRING, "force-play-query", "Song name/playlist/link", true));
    }
}

