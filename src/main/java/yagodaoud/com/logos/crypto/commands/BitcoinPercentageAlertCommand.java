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

import java.util.List;

import static yagodaoud.com.logos.tools.EmbedErrorMessageBuilder.getWrongOptionTypeMessage;
import static yagodaoud.com.logos.tools.MessageEmbedBuilder.messageEmbedBuilder;

@Component
public class BitcoinPercentageAlertCommand implements CommandHandlerInterface {

    private boolean isActive = false;
    private double percentage;
    private double currentPrice;
    private double lastPrice;
    private MessageChannel channel;


    @Autowired
    public BitcoinPercentageAlertCommand(CommandRegistryService commandRegistry) {
        commandRegistry.registerCommand(this);
    }

    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        if (!isActive) {
            try {
                percentage = event.getOption("percentage").getAsDouble();
                event.replyEmbeds(messageEmbedBuilder("Tracking Bitcoin price when its variation is greater than " + percentage + "%!", Colors.SUCCESS)).queue();
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
        return "bitcoin-percentage-alert";
    }

    @Override
    public String getDescription() {
        return "Create a percentage tracker for Bitcoin";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(new OptionData(OptionType.STRING, "percentage", "Percentage that will trigger the alert (in %)", true));
    }

    public String generateNotificationMessage() {
        String message = null;
        double variation =  this.priceVariationCalculator();

        if (Math.abs(variation) >= percentage) {
            lastPrice = currentPrice;
            String direction = variation > 0 ? "up" : "down";
            String emoji = variation > 0 ? "📈" : "📉";
            message = String.format("Bitcoin is %s! $%,.2f, (%.2f%% in the last hour) %s", direction, currentPrice, variation, emoji);
        }
        return message;
    }

    public void updateCurrentPrice(String cryptoPrice) {
        currentPrice = parsePrice(cryptoPrice);
    }

    public void sendNotificationMessage(String message) {
        if (!isActive || message == null) {
            return;
        }
        channel.sendMessageEmbeds(messageEmbedBuilder(message, Colors.SUCCESS)).queue();
        isActive = false;
    }

    public double priceVariationCalculator(){
        /*Formula to get the
          variation e.g. The price was 100,
          now it's 120 -> (120 - 100) / 100 = 0.2 * 100 = 20%
        */
        return ((currentPrice - lastPrice) / lastPrice) * 100;
    }

    private double parsePrice(String cryptoPrice) {
        return Double.parseDouble(cryptoPrice.replaceAll("[^\\d.]", ""));
    }
}
