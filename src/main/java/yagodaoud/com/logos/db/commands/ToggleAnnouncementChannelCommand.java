package yagodaoud.com.logos.db.commands;

import net.dv8tion.jda.api.Permission;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yagodaoud.com.logos.commands.CommandHandlerInterface;
import yagodaoud.com.logos.commands.CommandRegistryService;
import yagodaoud.com.logos.db.entity.AnnouncementChannel;
import yagodaoud.com.logos.db.repository.AnnouncementChannelRepository;
import yagodaoud.com.logos.tools.Colors;

import java.util.List;

import static yagodaoud.com.logos.tools.EmbedErrorMessageBuilder.getNotAdminEmbedMessage;
import static yagodaoud.com.logos.tools.MessageEmbedBuilder.messageEmbedBuilder;

@Component
public class ToggleAnnouncementChannelCommand implements CommandHandlerInterface {

    private final AnnouncementChannelRepository announcementChannelRepository;
    @Autowired
    public ToggleAnnouncementChannelCommand(CommandRegistryService commandRegistry, AnnouncementChannelRepository announcementChannelRepository) {
        commandRegistry.registerCommand(this);
        this.announcementChannelRepository = announcementChannelRepository;
    }
    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {
        if (!event.isFromGuild()) {
            event.replyEmbeds(messageEmbedBuilder("This command cannot be used in a direct message.", Colors.ADVERT)).queue();
            return;
        }

        if (!event.getMember().getPermissions().contains(Permission.ADMINISTRATOR)) {
            event.replyEmbeds(getNotAdminEmbedMessage()).queue();
            return;
        }

        AnnouncementChannel announcementChannelVerify = announcementChannelRepository.findByGuildId(event.getGuild().getId());

        if (announcementChannelVerify != null && !announcementChannelVerify.getChannelId().equals(event.getChannel().getId())) {
            event.replyEmbeds(messageEmbedBuilder("There can only be one announcement channel per server. The current channel is: <#" + announcementChannelVerify.getChannelId() + ">", Colors.ADVERT)).queue();
            return;
        }

        if (announcementChannelRepository.findByChannelIdAndGuildId(event.getChannel().getId(), event.getGuild().getId()) == null) {
            AnnouncementChannel announcementChannel = new AnnouncementChannel();
            announcementChannel.setChannelId(event.getChannel().getId());
            announcementChannel.setGuildId(event.getGuild().getId());
            announcementChannelRepository.save(announcementChannel);
            event.replyEmbeds(messageEmbedBuilder("Enabled the announcement channel in this server!", Colors.SUCCESS)).queue();
            return;
        }

        announcementChannelRepository.deleteByChannelId(event.getChannel().getId());
        event.replyEmbeds(messageEmbedBuilder("Disabled the announcement channel in this server!", Colors.ADVERT)).queue();
    }

    @Override
    public String getName() {
        return "toggle-announcement-channel";
    }

    @Override
    public String getDescription() {
        return "Set or unset the announcement channel for important messages from the dev team.";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of();
    }
}
