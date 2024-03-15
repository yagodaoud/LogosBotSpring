package yagodaoud.com.logos.music;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yagodaoud.com.logos.commands.CommandHandlerInterface;
import yagodaoud.com.logos.commands.CommandRegistryService;

import java.util.List;

@Component
public class LeaveChannelCommand implements CommandHandlerInterface {

    @Autowired
    public LeaveChannelCommand(CommandRegistryService commandRegistry) {
        commandRegistry.registerCommand(this);
    }

    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        AudioEventHandler audioEventHandler = new AudioEventHandler(event.getMember().getVoiceState());
        event.reply(audioEventHandler.leaveVoiceChannel()).queue();
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
