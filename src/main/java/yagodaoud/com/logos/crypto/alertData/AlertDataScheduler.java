package yagodaoud.com.logos.crypto.alertData;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import yagodaoud.com.logos.tools.Colors;

import static yagodaoud.com.logos.tools.MessageEmbedBuilder.messageEmbedBuilder;

public class AlertDataScheduler {
    private boolean isActive;
    private final MessageChannel channel;

    public AlertDataScheduler(MessageChannel channel) {
        this.channel = channel;
        this.isActive = true;
    }
    public void sendBitcoinPrice(String bitcoinPrice) {
        if (!isActive) {
            return;
        }
        channel.sendMessageEmbeds(messageEmbedBuilder("The closing price of Bitcoin is " + bitcoinPrice, Colors.SUCCESS)).queue();
    }

    public boolean getActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }
}
