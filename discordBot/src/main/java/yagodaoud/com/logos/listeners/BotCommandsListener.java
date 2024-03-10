package yagodaoud.com.logos.listeners;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yagodaoud.com.logos.commands.CommandHandlerInterface;
import yagodaoud.com.logos.commands.CommandRegistryService;

import java.util.HashMap;
import java.util.Map;

@Component
public class BotCommandsListener extends ListenerAdapter {
    private final CommandRegistryService commandRegistry;
    private final Map<String, CommandHandlerInterface> commandHandlers;

    @Autowired
    public BotCommandsListener(CommandRegistryService commandRegistry) {
        this.commandRegistry = commandRegistry;
        this.commandHandlers = initializeCommandHandlers();
    }

    private Map<String, CommandHandlerInterface> initializeCommandHandlers() {
        Map<String, CommandHandlerInterface> handlers = new HashMap<>();
        for (CommandHandlerInterface handler : commandRegistry.getCommandHandlers()) {
            handlers.put(handler.getName(), handler);
        }
        return handlers;
    }
    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {
        String commandName = event.getName();
        CommandHandlerInterface handler = commandHandlers.get(commandName);
        if (handler != null) {
            handler.handleCommand(event);
        } else {
            // Handle unknown command
            event.reply("Unknown command: " + commandName).queue();
        }
    }
}
