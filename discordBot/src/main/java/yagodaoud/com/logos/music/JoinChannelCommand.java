package yagodaoud.com.logos.music;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yagodaoud.com.logos.commands.CommandHandlerInterface;
import yagodaoud.com.logos.commands.CommandRegistryService;

import java.util.List;

@Component
public class JoinChannelCommand implements CommandHandlerInterface {

    @Autowired
    public JoinChannelCommand(CommandRegistryService commandRegistry) {
        commandRegistry.registerCommand(this);
    }

    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        AudioEventHandler audioEventHandler = new AudioEventHandler(event.getMember().getVoiceState());
        event.reply(audioEventHandler.joinVoiceChannel()).queue();
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
