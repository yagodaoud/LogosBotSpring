package yagodaoud.com.logos.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;

import java.util.List;

public interface CommandHandler {
    void handleCommand(SlashCommandInteractionEvent event, String[] args);

    String getName();
    List<String> getAliases();
}
