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
public class LeaveChannelCommand implements CommandHandlerInterface {

    private ApplicationEventPublisher eventPublisher;
    @Autowired
    public LeaveChannelCommand(ApplicationEventPublisher eventPublisher, CommandRegistryService commandRegistry) {
        this.eventPublisher = eventPublisher;
        commandRegistry.registerCommand(this);
    }

    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        AudioEventHandler audioEventHandler = new AudioEventHandler(eventPublisher, event.getMember().getVoiceState());
        event.replyEmbeds(audioEventHandler.leaveVoiceChannel()).queue();
    }

    @Override
    public String getName() {
        return "leave";
    }

    @Override
    public String getDescription() {
        return "Leave the voice channel.";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }
}
