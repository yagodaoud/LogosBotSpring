package yagodaoud.com.logos.music.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;
import yagodaoud.com.logos.commands.CommandHandlerInterface;
import yagodaoud.com.logos.commands.CommandRegistryService;
import yagodaoud.com.logos.music.audio.AudioEventHandler;

import java.util.List;

@Component
public class JoinChannelCommand implements CommandHandlerInterface {

    private final ApplicationEventPublisher eventPublisher;
    @Autowired
    public JoinChannelCommand(ApplicationEventPublisher applicationEventPublisher, CommandRegistryService commandRegistry) {
        this.eventPublisher = applicationEventPublisher;
        commandRegistry.registerCommand(this);
    }

    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        AudioEventHandler audioEventHandler = new AudioEventHandler(eventPublisher, event.getMember().getVoiceState());
        event.replyEmbeds(audioEventHandler.joinVoiceChannel()).queue();
    }

    @Override
    public String getName() {
        return "join";
    }

    @Override
    public String getDescription() {
        return "Join the current voice channel.";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }
}
