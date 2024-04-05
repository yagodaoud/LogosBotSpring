package yagodaoud.com.logos.crypto.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import yagodaoud.com.logos.commands.CommandHandlerInterface;
import yagodaoud.com.logos.commands.CommandRegistryService;
import yagodaoud.com.logos.crypto.services.CoinMarketCapApiService;

import java.util.List;

@Component
public class FetchCryptoPriceCommand implements CommandHandlerInterface {
    CoinMarketCapApiService coinMarketCapApiService = new CoinMarketCapApiService(new RestTemplate());
    public FetchCryptoPriceCommand(CommandRegistryService commandRegistry) {
        commandRegistry.registerCommand(this);
    }

    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        String cryptoSymbol = event.getOption("crypto-symbol").getAsString().toUpperCase();
        String cryptoPrice = coinMarketCapApiService.getCryptoPrice(cryptoSymbol);
        event.reply("The current price of " + cryptoSymbol + " is " + cryptoPrice).queue();
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
