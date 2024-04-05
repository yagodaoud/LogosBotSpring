package yagodaoud.com.logos.listeners;

import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import yagodaoud.com.logos.commands.CommandRegistryService;

public class LoadCommandsListener extends ListenerAdapter {
    private CommandRegistryService commandRegistry;

    @Autowired
    public LoadCommandsListener(CommandRegistryService commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        event.getJDA().updateCommands().addCommands(this.commandRegistry.getCommands()).queue();
    }
}
