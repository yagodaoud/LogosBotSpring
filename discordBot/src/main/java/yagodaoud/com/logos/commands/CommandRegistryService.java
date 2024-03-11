package yagodaoud.com.logos.commands;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class CommandRegistryService {

    private final Map<String, String> commandMap = new HashMap<>();
    private final List<CommandHandlerInterface> commandHandlerlist = new ArrayList<>();
    private final Map<String, OptionData> optionsMap = new HashMap<>();

    public void registerCommand(CommandHandlerInterface command) {
        commandMap.put(command.getName(), command.getDescription());
        commandHandlerlist.add(command);
        if (command.getOptions() != null) {
            command.getOptions().forEach(option -> optionsMap.put(command.getName(), option));
        }
    }

    public List<CommandData> getCommands() {
        return commandMap.entrySet().stream()
                .map(entry -> {
                    SlashCommandData slashCommandData = Commands.slash(entry.getKey(), entry.getValue());
                    slashCommandData.addOptions(optionsMap.entrySet().stream()
                            .filter(optionEntry -> optionEntry.getKey().equals(entry.getKey()))
                            .map(Map.Entry::getValue)
                            .toArray(OptionData[]::new));
                    return slashCommandData;
                })
                .collect(Collectors.toList());
    }

    public List<CommandHandlerInterface> getCommandHandlers() {
        return this.commandHandlerlist;
    }
}
