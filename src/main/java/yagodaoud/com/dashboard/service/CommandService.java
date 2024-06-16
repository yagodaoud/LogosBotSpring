package yagodaoud.com.dashboard.service;

import org.springframework.stereotype.Service;
import yagodaoud.com.logos.commands.CommandHandlerInterface;
import yagodaoud.com.logos.commands.CommandRegistryService;
import yagodaoud.com.logos.commands.CryptoCommandInterface;
import yagodaoud.com.logos.commands.MusicCommandInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommandService {

    public List<List<? extends CommandHandlerInterface>> getCommands(CommandRegistryService commandRegistryService) {
        List<CommandHandlerInterface> commands = commandRegistryService.getCommandHandlers();
        List<List<? extends CommandHandlerInterface>> filteredLists = new ArrayList<>();

        List<CryptoCommandInterface> cryptoCommands = commands.stream()
                .filter(command -> command instanceof CryptoCommandInterface)
                .map(command -> (CryptoCommandInterface) command)
                .collect(Collectors.toList());

        filteredLists.add(cryptoCommands);

        List<MusicCommandInterface> musicCommands = commands.stream()
                .filter(command -> command instanceof MusicCommandInterface)
                .map(command -> (MusicCommandInterface) command)
                .collect(Collectors.toList());

        filteredLists.add(musicCommands);

        return filteredLists;
    }
}
