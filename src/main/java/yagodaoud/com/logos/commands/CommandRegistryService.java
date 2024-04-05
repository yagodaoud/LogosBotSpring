package yagodaoud.com.logos.commands;

import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.Commands;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.interactions.commands.build.SlashCommandData;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CommandRegistryService {

    private final Map<String, String> commandMap = new HashMap<>();
    private final List<CommandHandlerInterface> commandHandlerlist = new ArrayList<>();
    private final Map<String, List<OptionData>> optionsMap = new HashMap<>();

    public void registerCommand(CommandHandlerInterface command) {
        commandMap.put(command.getName(), command.getDescription());
        commandHandlerlist.add(command);
        List<OptionData> options = command.getOptions();
        if (options != null && !options.isEmpty()) {
            optionsMap.put(command.getName(), options);
        }
    }

    public List<CommandData> getCommands() {
        return commandMap.entrySet().stream()
                .map(entry -> {
                    SlashCommandData slashCommandData = Commands.slash(entry.getKey(), entry.getValue());
                    List<OptionData> options = optionsMap.getOrDefault(entry.getKey(), Collections.emptyList());
                    slashCommandData.addOptions(options.toArray(new OptionData[0]));
                    return slashCommandData;
                })
                .collect(Collectors.toList());
    }

    public List<CommandHandlerInterface> getCommandHandlers() {
        return this.commandHandlerlist;
    }
}
