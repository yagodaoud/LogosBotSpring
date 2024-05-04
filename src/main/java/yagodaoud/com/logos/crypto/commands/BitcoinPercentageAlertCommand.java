package yagodaoud.com.logos.crypto.commands;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yagodaoud.com.logos.commands.CommandHandlerInterface;
import yagodaoud.com.logos.commands.CommandRegistryService;
import yagodaoud.com.logos.crypto.alertData.AlertDataPercentage;
import yagodaoud.com.logos.tools.Colors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static yagodaoud.com.logos.tools.EmbedErrorMessageBuilder.getWrongOptionTypeMessage;
import static yagodaoud.com.logos.tools.MessageEmbedBuilder.messageEmbedBuilder;

@Component
public class BitcoinPercentageAlertCommand implements CommandHandlerInterface {
    public final Map<Long, AlertDataPercentage> alertDataMap = new HashMap<>();

    @Autowired
    public BitcoinPercentageAlertCommand(CommandRegistryService commandRegistry) {
        commandRegistry.registerCommand(this);
    }

    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        try {
            double percentage = event.getOption("percentage").getAsDouble();
            MessageChannel channel = event.isFromGuild() ? event.getChannel().asTextChannel() : event.getChannel().asPrivateChannel();

            AlertDataPercentage alertDataPercentage = alertDataMap.get(channel.getIdLong());

            if (alertDataPercentage == null || !alertDataPercentage.getActive()) {
                alertDataPercentage = new AlertDataPercentage(percentage, channel);
                alertDataMap.put(channel.getIdLong(), alertDataPercentage);
                event.replyEmbeds(messageEmbedBuilder("Tracking Bitcoin price when its variation is greater than " + percentage + "%!", Colors.SUCCESS)).queue();
                return;
            }
            event.replyEmbeds(messageEmbedBuilder("The command is already active.", Colors.ADVERT)).queue();
        } catch (NumberFormatException exception) {
            event.replyEmbeds(getWrongOptionTypeMessage("number")).queue();
        }
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
}
