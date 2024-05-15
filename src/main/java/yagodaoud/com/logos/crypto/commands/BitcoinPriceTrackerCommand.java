package yagodaoud.com.logos.crypto.commands;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yagodaoud.com.logos.commands.CommandHandlerInterface;
import yagodaoud.com.logos.commands.CommandRegistryService;
import yagodaoud.com.logos.crypto.alertData.AlertDataTracker;
import yagodaoud.com.logos.tools.Colors;

import java.text.NumberFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static yagodaoud.com.logos.tools.EmbedErrorMessageBuilder.getWrongOptionTypeMessage;
import static yagodaoud.com.logos.tools.MessageEmbedBuilder.messageEmbedBuilder;

@Component
public class BitcoinPriceTrackerCommand implements CommandHandlerInterface{
    public final Map<String, Map<Long, AlertDataTracker>> alertDataMap = new HashMap<>();

    @Autowired
    public BitcoinPriceTrackerCommand(CommandRegistryService commandRegistry) {
        commandRegistry.registerCommand(this);
    }

    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        try {
            double targetPrice = event.getOption("target-price").getAsDouble();
            MessageChannel channel = event.isFromGuild() ? event.getChannel().asTextChannel() : event.getChannel().asPrivateChannel();
            String userId = event.getUser().getId();

            Map<Long, AlertDataTracker> userAlertData = alertDataMap.computeIfAbsent(userId, k -> new HashMap<>());
            AlertDataTracker alertDataTracker = alertDataMap.get(userId).get(channel.getIdLong());

            if (alertDataTracker == null || !alertDataTracker.getActive()) {
                alertDataTracker = new AlertDataTracker(targetPrice, channel, userId);

                userAlertData.put(channel.getIdLong(), alertDataTracker);
                alertDataMap.put(userId, userAlertData);
                event.replyEmbeds(messageEmbedBuilder("Tracking bitcoin price when it reaches " + NumberFormat.getCurrencyInstance(Locale.US).format(targetPrice), Colors.SUCCESS)).queue();
                return;
            }
            event.replyEmbeds(messageEmbedBuilder("The command is already active.", Colors.ADVERT)).queue();
        } catch (NumberFormatException exception) {
            event.replyEmbeds(getWrongOptionTypeMessage("number")).queue();
        }
    }

    @Override
    public String getName() {
        return "bitcoin-price-tracker";
    }

    @Override
    public String getDescription() {
        return "If the value is reached, the bot will send a notification";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(new OptionData(OptionType.STRING, "target-price", "Target price desired", true));
    }

    public Map<String, Map<Long, AlertDataTracker>> getAlertDataMap() {
        return this.alertDataMap;
    }
}
