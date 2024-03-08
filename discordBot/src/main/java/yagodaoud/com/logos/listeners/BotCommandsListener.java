package yagodaoud.com.logos.listeners;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import org.springframework.stereotype.Component;
import yagodaoud.com.logos.commands.CommandRegistry;

import java.util.List;

@Component
public class BotCommandsListener extends ListenerAdapter {

    private final CommandRegistry commandRegistry;

    public BotCommandsListener(CommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
    }
    @Override
    public void onSlashCommandInteraction(SlashCommandInteractionEvent event) {
        String commandName = event.getName();
        String[] args = event.getOptions().stream()
                .map(OptionMapping::getAsString)
                .toArray(String[]::new);

        commandRegistry.handleCommand(event, commandName, args);
    }
}
