package yagodaoud.com.logos.tools;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.awt.*;

public abstract class MessageEmbedBuilder {

    public static MessageEmbed messageEmbedBuilder(String message, Color color) {
        EmbedBuilder embedBuilder = new EmbedBuilder();
        embedBuilder.setDescription(message);
        embedBuilder.setColor(color);

        return embedBuilder.build();
    }

}
