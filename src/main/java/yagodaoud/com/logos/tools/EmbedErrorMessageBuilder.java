package yagodaoud.com.logos.tools;

import net.dv8tion.jda.api.entities.MessageEmbed;

import static yagodaoud.com.logos.tools.MessageEmbedBuilder.messageEmbedBuilder;

public abstract class EmbedErrorMessageBuilder {
    public static MessageEmbed getUnknownCommandEmbedMessage(String command) {
        return messageEmbedBuilder("Unknown command: " + command + ".", Colors.ADVERT);
    }
    public static MessageEmbed getSomethingWentWrongEmbedMessage() {
        return messageEmbedBuilder("Something went wrong.", Colors.ADVERT);
    }
    public static MessageEmbed getPlayerNotStartedEmbedMessage() {
        return messageEmbedBuilder("You must start the player first.", Colors.ADVERT);
    }
    public static MessageEmbed getNotAdminEmbedMessage() {
        return messageEmbedBuilder("You must be an admin to use this command.", Colors.ADVERT);
    }
    public static MessageEmbed getWrongOptionTypeMessage(String type) {
        return messageEmbedBuilder("The command option must be a " + type + ".", Colors.ADVERT);
    }
}
