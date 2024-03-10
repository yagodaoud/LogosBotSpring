package yagodaoud.com.logos.crypto;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import yagodaoud.com.logos.commands.CommandHandlerInterface;
import yagodaoud.com.logos.commands.CommandRegistryService;

import java.util.List;

public class CryptoPrice implements CommandHandlerInterface {

    public CryptoPrice(CommandRegistryService commandRegistry) {
        commandRegistry.registerCommand(this);
    }

    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        event.reply(event.getOption("crypto-symbol").getAsString()).queue();
    }

    @Override
    public String getName() {
        return "crypto-price";
    }

    @Override
    public String getDescription() {
        return "get the crypto price";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(new OptionData(OptionType.STRING, "crypto-symbol", "Enter the crypto symbol", true));
    }

}
