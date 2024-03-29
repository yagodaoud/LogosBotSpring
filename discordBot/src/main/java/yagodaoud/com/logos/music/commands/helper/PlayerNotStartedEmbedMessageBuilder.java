package yagodaoud.com.logos.music.commands.helper;

import net.dv8tion.jda.api.entities.MessageEmbed;
import yagodaoud.com.logos.helper.Colors;

import static yagodaoud.com.logos.helper.MessageEmbedBuilder.messageEmbedBuilder;

public abstract class PlayerNotStartedEmbedMessageBuilder {

    public static MessageEmbed getPlayerNotStartedEmbedMessage() {
        return messageEmbedBuilder("You must start the player first", Colors.ADVERT);
    }
}
