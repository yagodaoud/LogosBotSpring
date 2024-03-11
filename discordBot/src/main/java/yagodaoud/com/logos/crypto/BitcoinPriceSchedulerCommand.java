package yagodaoud.com.logos.crypto;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import yagodaoud.com.logos.commands.CommandHandlerInterface;
import yagodaoud.com.logos.commands.CommandRegistryService;

import java.util.List;

@Component
@Scope("singleton")
public class BitcoinPriceSchedulerCommand implements CommandHandlerInterface {
    private final CoinMarketCapApiService coinMarketCapApiService = new CoinMarketCapApiService(new RestTemplate());
    protected boolean isActive = false;
    protected TextChannel textChannel;

    @Autowired
    public BitcoinPriceSchedulerCommand(CommandRegistryService commandRegistryService) {
        commandRegistryService.registerCommand(this);
    }

    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        if (!isActive) {
            System.out.println("BitcoinPriceSchedulerCommand instance: " + this);
            event.reply("The daily closing price of Bitcoin will be displayed from now on!").queue();
            textChannel = event.getChannel().asTextChannel();
            sendBitcoinPrice(textChannel);
            isActive = true;
            return;
        }
        event.reply("Disabled the daily closing price of Bitcoin on this channel").queue();
    }

    @Override
    public String getName() {
        return "bitcoin-price-scheduler";
    }

    @Override
    public String getDescription() {
        return "Send the price of Bitcoin at 12:00 AM UTC everyday";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    public void sendBitcoinPrice(TextChannel channel) {
        if (!isActive) {
            return;
        }
        String bitcoinPrice = coinMarketCapApiService.getCryptoPrice("BTC");
        channel.sendMessage("The closing price of Bitcoin is " + bitcoinPrice).queue();
    }
}
