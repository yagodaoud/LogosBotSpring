package yagodaoud.com.dashboard.helper;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import yagodaoud.com.logos.tools.Colors;

import java.util.List;

public class DefaultAnnouncementView {

    private static final String supportServerUrl = "https://discord.gg/b7mr2T2WfB";

    public static MessageCreateData getDefaultAnnouncementView(JDA bot, String message) {
        String botInviteUrl = bot.getInviteUrl(List.of(Permission.USE_APPLICATION_COMMANDS, Permission.MESSAGE_SEND, Permission.VIEW_CHANNEL, Permission.VOICE_SPEAK));

        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("Logos#5402", botInviteUrl, "https://cdn-icons-png.flaticon.com/512/749/749024.png?w=826&t=st=1689123378~exp=1689123978~hmac=e975ec34409fd04e619d0f301ab29850bfdebdc13c749d2ee39b9f00bd8dcf9c")
                .setColor(Colors.DEFAULT_HELP_MENU)
                .setThumbnail("https://cdn-icons-png.flaticon.com/512/749/749024.png?w=826&t=st=1689123378~exp=1689123978~hmac=e975ec34409fd04e619d0f301ab29850bfdebdc13c749d2ee39b9f00bd8dcf9c")
                .setTitle("New announcement from the dev team!")
                .setDescription(message)
                .appendDescription("\n \u200E")
                .addField("Send your Feedback!", "yagodaouddev@gmail.com or `krdzz` on discord ", false)
                .addField("Need help?", "Join the official [support server](" + supportServerUrl + ")", false)
                .setFooter("To disable this alert, 'use toggle-announcement-channel' again in this channel.")
                .build();

        return new MessageCreateBuilder()
                .addEmbeds(builder.build()).build();

    }
}
