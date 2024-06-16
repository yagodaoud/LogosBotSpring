package yagodaoud.com.dashboard.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import yagodaoud.com.dashboard.service.CommandService;
import yagodaoud.com.logos.commands.CommandHandlerInterface;
import yagodaoud.com.logos.commands.CommandRegistryService;

import java.util.List;

@Controller
@Lazy
public class CommandController {
    @Autowired
    private CommandRegistryService commandRegistry;

    @Autowired
    private CommandService commandService;

    @GetMapping("/commands")
    public String commands(Model model) {
        List<List<? extends CommandHandlerInterface>> commandsLists = commandService.getCommands(commandRegistry);
        List<? extends CommandHandlerInterface> cryptoCommands = commandsLists.get(0);
        List<? extends CommandHandlerInterface> musicCommands = commandsLists.get(1);

        model.addAttribute("cryptoCommands", cryptoCommands);
        model.addAttribute("musicCommands", musicCommands);
        return "commands";
    }
}
