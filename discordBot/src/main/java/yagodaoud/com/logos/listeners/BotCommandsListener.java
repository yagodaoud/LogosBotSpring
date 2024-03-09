package yagodaoud.com.logos.listeners;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.stereotype.Component;
import yagodaoud.com.logos.commands.CommandRegistryService;

@Component
public class BotCommandsListener extends ListenerAdapter {
    private final CommandRegistryService commandRegistry;
    public BotCommandsListener(CommandRegistryService commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void onSlashCommandInteraction(@NotNull SlashCommandInteractionEvent event) {

    }
}
