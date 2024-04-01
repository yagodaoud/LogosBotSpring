package yagodaoud.com.logos.music.commands.helper;

import net.dv8tion.jda.api.entities.MessageEmbed;
import yagodaoud.com.logos.tools.Colors;

import static yagodaoud.com.logos.tools.MessageEmbedBuilder.messageEmbedBuilder;

public abstract class EmbedErrorMessageBuilder {

    public static MessageEmbed getPlayerNotStartedEmbedMessage() {
        return messageEmbedBuilder("You must start the player first.", Colors.ADVERT);
    }
    public static MessageEmbed getNotAdminEmbedMessage() {
        return messageEmbedBuilder("You must be an admin to use this command.", Colors.ADVERT);
    }
}
