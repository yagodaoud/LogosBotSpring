package yagodaoud.com.logos.crypto.commands;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yagodaoud.com.logos.commands.CommandHandlerInterface;
import yagodaoud.com.logos.commands.CommandRegistryService;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

@Component
public class BitcoinPriceTrackerCommand implements CommandHandlerInterface{
    private double targetPrice;
    private double currentPrice;
    private int priceTrend;
    private String userId;
    private TextChannel channel;
    private boolean isActive = false;

    @Autowired
    public BitcoinPriceTrackerCommand(CommandRegistryService commandRegistry) {
        commandRegistry.registerCommand(this);
    }

    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        if (!isActive) {
            targetPrice = event.getOption("target-price").getAsDouble();
            event.reply("Tracking bitcoin price when it reaches " + NumberFormat.getCurrencyInstance(Locale.US).format(targetPrice)).queue();
            userId = event.getUser().getId();
            channel = event.getChannel().asTextChannel();
            isActive = true;
            return;
        }
        event.reply("The command is already active.").queue();

    }

    @Override
    public String getName() {
        return "bitcoin-price-tracker";
    }

    @Override
    public String getDescription() {
        return "If the value is reached, the bot will send a notification";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(new OptionData(OptionType.STRING, "target-price", "Target price desired", true));
    }

    public void updateCurrentPrice(String cryptoPrice) {
        currentPrice = parsePrice(cryptoPrice);
    }

    public void updatePriceTrend() {
        double previousPrice = currentPrice;

        if (currentPrice > previousPrice) {
            priceTrend = 1; // Uptrend
        } else if (currentPrice < previousPrice) {
            priceTrend = 0; // Downtrend
        } else {
            priceTrend = -1; // No change
        }
    }

    public String generateNotificationMessage() {
        String message = null;

        if ((currentPrice > targetPrice && priceTrend == 1) || (currentPrice < targetPrice && priceTrend == 0)) {
            String direction = priceTrend == 1 ? "exceeded" : "gone below";
            message = String.format("Bitcoin has %s $%,.2f, now at $%,.2f <@%s>!", direction, targetPrice, currentPrice, userId);
        }
        return message;
    }

    public void sendNotificationMessage(String message) {
        if (!isActive || message == null) {
            return;
        }
        channel.sendMessage(message).queue();
        isActive = false;
    }

    private double parsePrice(String cryptoPrice) {
        return Double.parseDouble(cryptoPrice.replaceAll("[^\\d.]", ""));
    }
}
