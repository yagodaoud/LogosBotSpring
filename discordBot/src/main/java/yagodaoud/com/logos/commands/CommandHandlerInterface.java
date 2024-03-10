package yagodaoud.com.logos.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;

import java.util.List;

public interface CommandHandlerInterface {
    void handleCommand(SlashCommandInteractionEvent event);

    String getName();
    String getDescription();
    List<OptionData> getOptions();
}
