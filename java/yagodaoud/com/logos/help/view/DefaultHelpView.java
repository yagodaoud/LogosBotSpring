package yagodaoud.com.logos.help.view;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.components.ActionRow;
import net.dv8tion.jda.api.interactions.components.selections.StringSelectMenu;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import yagodaoud.com.logos.tools.Colors;

import java.util.List;

public class DefaultHelpView {

    public static MessageCreateData getHelpView(SlashCommandInteractionEvent event){
        EmbedBuilder builder = new EmbedBuilder();
        builder.setAuthor("Logos Bot", event.getJDA().getInviteUrl(List.of(Permission.USE_APPLICATION_COMMANDS, Permission.MESSAGE_SEND, Permission.VIEW_CHANNEL, Permission.VOICE_SPEAK)), "https://cdn-icons-png.flaticon.com/512/749/749024.png?w=826&t=st=1689123378~exp=1689123978~hmac=e975ec34409fd04e619d0f301ab29850bfdebdc13c749d2ee39b9f00bd8dcf9c")
                .appendDescription("\nOpen source discord bot with music features and crypto commands! Play your favorite songs directly from YouTube/SoundCloud.")
                .setThumbnail("https://cdn-icons-png.flaticon.com/512/749/749024.png?w=826&t=st=1689123378~exp=1689123978~hmac=e975ec34409fd04e619d0f301ab29850bfdebdc13c749d2ee39b9f00bd8dcf9c")
                .setColor(Colors.DEFAULT_HELP_MENU)
                .addField("GitHub Repository", "https://github.com/yagodaoud/LogosBotSpring", false)
                .addField("Send your Feedback!", "yagodaouddev@gmail.com or `krdzz` on discord ", false)
                .addField("Need help?", "Join the official [support server](https://discord.gg/b7mr2T2WfB) or get help below:", false)
                .build();

        ActionRow actionRow = ActionRow.of(
                StringSelectMenu.create("menu:help")
                        .setPlaceholder("Select the command type you need help with")
                        .addOption("\uD83C\uDFB5 Music", "music")
                        .addOption("\uD83D\uDCB0 Crypto", "crypto")
                        .setRequiredRange(1, 1)
                        .build().asEnabled());

        MessageCreateBuilder messageBuilder = new MessageCreateBuilder()
                .addEmbeds(builder.build())
                .addComponents(actionRow);

        return messageBuilder.build();
    }
}
