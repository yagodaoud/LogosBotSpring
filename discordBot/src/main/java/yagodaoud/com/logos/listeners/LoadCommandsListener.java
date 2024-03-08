package yagodaoud.com.logos.listeners;

import net.dv8tion.jda.api.events.session.ReadyEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import yagodaoud.com.logos.commands.CommandHandler;
import yagodaoud.com.logos.commands.CommandRegistry;
import yagodaoud.com.logos.crypto.CryptoPrice;

import java.util.ArrayList;
import java.util.Collection;

public class LoadCommandsListener extends ListenerAdapter {
    private final CommandRegistry commandRegistry;
    public LoadCommandsListener(CommandRegistry commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    @Override
    public void onReady(ReadyEvent event) {
        CommandRegistry commandRegistry = new CommandRegistry();
        commandRegistry.registerCommand("crypto-price", new CryptoPrice());
//        commandRegistry.registerCommand("stop", new StopCommand());

        ArrayList<CommandData> commandHandlers = (ArrayList<CommandHandler>) commandRegistry.getCommandHandlers().values();

        event.getJDA().updateCommands().addCommands(commandHandlers).queue();
    }
}
