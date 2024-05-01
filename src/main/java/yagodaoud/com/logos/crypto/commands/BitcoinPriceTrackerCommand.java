package yagodaoud.com.logos.crypto.commands;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yagodaoud.com.logos.commands.CommandHandlerInterface;
import yagodaoud.com.logos.commands.CommandRegistryService;
import yagodaoud.com.logos.tools.Colors;

import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

import static yagodaoud.com.logos.tools.EmbedErrorMessageBuilder.getWrongOptionTypeMessage;
import static yagodaoud.com.logos.tools.MessageEmbedBuilder.messageEmbedBuilder;

@Component
public class BitcoinPriceTrackerCommand implements CommandHandlerInterface{
    private double targetPrice;
    private double currentPrice;
    private int priceTrend;
    private String userId;
    private MessageChannel channel;
    private boolean isActive = false;

    @Autowired
    public BitcoinPriceTrackerCommand(CommandRegistryService commandRegistry) {
        commandRegistry.registerCommand(this);
    }

    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        if (!isActive) {
            try {
                targetPrice = event.getOption("target-price").getAsDouble();
                event.replyEmbeds(messageEmbedBuilder("Tracking bitcoin price when it reaches " + NumberFormat.getCurrencyInstance(Locale.US).format(targetPrice), Colors.SUCCESS)).queue();
                userId = event.getUser().getId();
                isActive = true;
                if (event.isFromGuild()) {
                    channel = event.getChannel().asTextChannel();
                    return;
                }
                channel = event.getChannel().asPrivateChannel();
                return;
            } catch (NumberFormatException exception) {
                event.replyEmbeds(getWrongOptionTypeMessage("number")).queue();
                return;
            }
        }
        event.replyEmbeds(messageEmbedBuilder("The command is already active.", Colors.ADVERT)).queue();

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

        priceTrend = -1; // No change

        if (currentPrice < previousPrice) {
            priceTrend = 0; // Downtrend
            return;
        }

        if (currentPrice > previousPrice) {
            priceTrend = 1; // Uptrend
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
        channel.sendMessageEmbeds(messageEmbedBuilder(message, Colors.SUCCESS)).queue();
        isActive = false;
    }

    private double parsePrice(String cryptoPrice) {
        return Double.parseDouble(cryptoPrice.replaceAll("[^\\d.]", ""));
    }
}
