package yagodaoud.com.logos.crypto.commands;

import net.dv8tion.jda.api.entities.channel.middleman.MessageChannel;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import yagodaoud.com.logos.commands.CommandHandlerInterface;
import yagodaoud.com.logos.commands.CommandRegistryService;
import yagodaoud.com.logos.commands.CryptoCommandInterface;
import yagodaoud.com.logos.crypto.alertData.AlertDataScheduler;
import yagodaoud.com.logos.crypto.alertData.AlertDataTracker;
import yagodaoud.com.logos.tools.Colors;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static yagodaoud.com.logos.tools.MessageEmbedBuilder.messageEmbedBuilder;

@Component
@Scope("singleton")
public class BitcoinPriceSchedulerCommand implements CommandHandlerInterface, CryptoCommandInterface {
    public final Map<Long, AlertDataScheduler> alertDataMap = new HashMap<>();

    @Autowired
    public BitcoinPriceSchedulerCommand(CommandRegistryService commandRegistryService) {
        commandRegistryService.registerCommand(this);
    }

    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        MessageChannel channel = event.isFromGuild() ? event.getChannel().asTextChannel() : event.getChannel().asPrivateChannel();

        AlertDataScheduler alertDataScheduler = alertDataMap.get(channel.getIdLong());

        if (alertDataScheduler == null || !alertDataScheduler.getActive()) {
            alertDataScheduler = new AlertDataScheduler(channel);
            alertDataMap.put(channel.getIdLong(), alertDataScheduler);
            event.replyEmbeds(messageEmbedBuilder("The daily closing price of Bitcoin will be displayed from now on!", Colors.SUCCESS)).queue();
            return;
        }
        event.replyEmbeds(messageEmbedBuilder("The command is already active.", Colors.ADVERT)).queue();
    }

    @Override
    public String getName() {
        return "bitcoin-price-scheduler";
    }

    @Override
    public String getDescription() {
        return "Send the price of Bitcoin at 12:00 AM UTC everyday";
    }

    @Override
    public List<OptionData> getOptions() {
        return null;
    }

    public Map<Long, AlertDataScheduler> getAlertDataMap() {
        return this.alertDataMap;
    }
}
