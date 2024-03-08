package yagodaoud.com.logos.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class CommandRegistry {
    private final Map<String, CommandHandler> commandHandlers = new HashMap<>();

    public void registerCommand(String commandName, CommandHandler handler) {
        commandHandlers.put(commandName, handler);
    }

    public void handleCommand(SlashCommandInteractionEvent event, String commandName, String[] args) {
        CommandHandler handler = commandHandlers.get(commandName);
        if (handler != null) {
            handler.handleCommand(event, args);
        }
    }

    public Map<String, CommandHandler> getCommandHandlers() {
        return this.commandHandlers;
    }
}
