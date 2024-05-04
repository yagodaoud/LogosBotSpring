package yagodaoud.com.logos.crypto.alertData;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import yagodaoud.com.logos.tools.Colors;

import static yagodaoud.com.logos.tools.MessageEmbedBuilder.messageEmbedBuilder;

public class AlertDataPercentage {
    private boolean isActive;
    private double percentage;
    private double currentPrice;
    private double lastPrice;
    private final MessageChannel channel;

    public AlertDataPercentage(double percentage, MessageChannel channel) {
        this.percentage = percentage;
        this.channel = channel;
        this.isActive = true;
    }

    public String generateNotificationMessage() {
        String message = null;
        double variation =  this.priceVariationCalculator();
        lastPrice = currentPrice;

        if (Double.isFinite(variation) && Math.abs(variation) >= percentage) {
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

    public double priceVariationCalculator() {
        /*Formula to get the
          variation e.g. The price was 100,
          now it's 120 -> (120 - 100) / 100 = 0.2 * 100 = 20%
        */
        return ((currentPrice - lastPrice) / lastPrice) * 100;
    }

    private double parsePrice(String cryptoPrice) {
        return Double.parseDouble(cryptoPrice.replaceAll("[^\\d.]", ""));
    }

    public boolean getActive() {
        return this.isActive;
    }
    public void setActive(boolean active) {
        isActive = active;
    }
}
