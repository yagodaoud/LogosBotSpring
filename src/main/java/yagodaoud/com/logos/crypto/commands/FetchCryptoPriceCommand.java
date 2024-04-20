package yagodaoud.com.logos.crypto.commands;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.json.JSONException;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import yagodaoud.com.logos.commands.CommandHandlerInterface;
import yagodaoud.com.logos.commands.CommandRegistryService;
import yagodaoud.com.logos.crypto.services.CoinMarketCapApiService;
import yagodaoud.com.logos.tools.Colors;

import java.util.List;

import static yagodaoud.com.logos.music.commands.helper.EmbedErrorMessageBuilder.getWrongOptionTypeMessage;
import static yagodaoud.com.logos.tools.MessageEmbedBuilder.messageEmbedBuilder;

@Component
public class FetchCryptoPriceCommand implements CommandHandlerInterface {
    CoinMarketCapApiService coinMarketCapApiService = new CoinMarketCapApiService(new RestTemplate());
    public FetchCryptoPriceCommand(CommandRegistryService commandRegistry) {
        commandRegistry.registerCommand(this);
    }

    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        String cryptoSymbol = event.getOption("crypto-symbol").getAsString().toUpperCase();
        try {
            String cryptoPrice = coinMarketCapApiService.getCryptoPrice(cryptoSymbol);
            event.replyEmbeds(messageEmbedBuilder("The current price of " + cryptoSymbol + " is " + cryptoPrice, Colors.SUCCESS)).queue();
        } catch (JSONException exception) {
            event.replyEmbeds(getWrongOptionTypeMessage("ticker symbol")).queue();
        }
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
