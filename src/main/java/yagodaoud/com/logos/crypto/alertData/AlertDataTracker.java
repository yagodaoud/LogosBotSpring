package yagodaoud.com.logos.crypto.alertData;

import lombok.Getter;
import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import org.springframework.web.client.RestTemplate;
import yagodaoud.com.logos.crypto.services.CoinMarketCapApiService;
import yagodaoud.com.logos.tools.Colors;

import static yagodaoud.com.logos.tools.MessageEmbedBuilder.messageEmbedBuilder;

public class AlertDataTracker {
    private boolean isActive;
    private final double targetPrice;
    private double currentPrice;
    @Getter
    private double priceTrend;
    private final String userId;
    private final MessageChannel channel;

    public AlertDataTracker(double targetPrice, MessageChannel channel, String userId) {
        this.targetPrice = targetPrice;
        this.channel = channel;
        this.userId = userId;
        this.isActive = true;
        updatePriceTrend(parsePrice(new CoinMarketCapApiService(new RestTemplate()).getCryptoPrice("BTC")));
    }

    public void updateCurrentPrice(String cryptoPrice) {
        currentPrice = parsePrice(cryptoPrice);
    }

    public void updatePriceTrend(double btcStartingPrice) {
        priceTrend = -1; // No change

        if (btcStartingPrice > targetPrice) {
            priceTrend = 0; // Downtrend
            return;
        }

        if (btcStartingPrice < targetPrice) {
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

    public boolean getActive() {
        return this.isActive;
    }
    public void setActive(boolean active) {
        isActive = active;
    }
}
