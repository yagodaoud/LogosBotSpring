package yagodaoud.com.logos.music;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import yagodaoud.com.logos.commands.CommandHandlerInterface;

import java.util.List;

public class PlayCommand implements CommandHandlerInterface {
    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {

    }

    @Override
    public String getName() {
        return "Play";
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }
}
