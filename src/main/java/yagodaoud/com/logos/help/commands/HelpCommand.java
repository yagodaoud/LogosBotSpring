package yagodaoud.com.logos.help.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yagodaoud.com.logos.commands.CommandHandlerInterface;
import yagodaoud.com.logos.commands.CommandRegistryService;

import java.util.List;

import static yagodaoud.com.logos.help.view.DefaultHelpView.getHelpView;

@Component
public class HelpCommand implements CommandHandlerInterface {

    @Autowired
    public HelpCommand (CommandRegistryService commandRegistryService) {
        commandRegistryService.registerCommand(this);
    }

    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        event.reply(getHelpView(event)).setEphemeral(true).queue();
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public String getDescription() {
        return "Get help with Logos' features.";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }
}
