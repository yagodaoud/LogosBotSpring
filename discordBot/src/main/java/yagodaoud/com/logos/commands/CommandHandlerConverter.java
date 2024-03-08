package yagodaoud.com.logos.commands;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;

public class CommandHandlerConverter {
    public static CommandData convertToCommandData(CommandHandler handler) {
        CommandData commandData = new CommandData(handler.getName(), "Description goes here");
        handler.getAliases().forEach(commandData::addOption); // Add aliases as options
        // Add other relevant options (parameters, etc.) to commandData
        return commandData;
    }
}
