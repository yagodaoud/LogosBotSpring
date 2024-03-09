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

    private final List<OptionData> options = new ArrayList<>();
    public void registerCommand(CommandHandlerInterface command) {
        commandMap.put(command.getName(), command.getDescription());
        options.add((OptionData) command.getOptions());
    }

    public List<CommandData> getCommands() {
        return commandMap.entrySet().stream()
                .map(entry -> {
                    SlashCommandData slashCommandData = Commands.slash(entry.getKey(), entry.getValue());
                    slashCommandData.addOptions(this.options);
                    return slashCommandData;
                })
                .collect(Collectors.toList());
    }
}
