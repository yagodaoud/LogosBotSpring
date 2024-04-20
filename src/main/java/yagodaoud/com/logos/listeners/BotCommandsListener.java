package yagodaoud.com.logos.listeners;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.events.interaction.component.StringSelectInteractionEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yagodaoud.com.logos.commands.CommandHandlerInterface;
import yagodaoud.com.logos.commands.CommandRegistryService;

import java.util.HashMap;
import java.util.Map;

import static yagodaoud.com.logos.help.view.HelpCryptoView.getCryptoView;
import static yagodaoud.com.logos.help.view.HelpMusicView.getMusicView;
import static yagodaoud.com.logos.tools.EmbedErrorMessageBuilder.getSomethingWentWrongEmbedMessage;
import static yagodaoud.com.logos.tools.EmbedErrorMessageBuilder.getUnknownCommandEmbedMessage;

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
        try {
            if (handler != null) {
                handler.handleCommand(event);
                return;
            }
            event.replyEmbeds(getUnknownCommandEmbedMessage(commandName)).queue();
        } catch (Exception exception) {
            event.replyEmbeds(getSomethingWentWrongEmbedMessage()).queue();
        }
    }

    @Override
    public void onStringSelectInteraction(StringSelectInteractionEvent event) {
        if (event.getComponentId().equals("menu:help")) {
            if (event.getValues().contains("music")) {
                event.reply(getMusicView()).setEphemeral(true).queue();
                return;
            }
            event.reply(getCryptoView()).setEphemeral(true).queue();
        }
    }
}
