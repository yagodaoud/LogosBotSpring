package yagodaoud.com.logos.music.audio;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import org.jetbrains.annotations.NotNull;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationEventPublisherAware;
import yagodaoud.com.logos.music.audio.event.VoiceChannelJoinEvent;
import yagodaoud.com.logos.music.audio.event.VoiceChannelLeaveEvent;
import yagodaoud.com.logos.tools.Colors;

import static yagodaoud.com.logos.tools.MessageEmbedBuilder.messageEmbedBuilder;

public class AudioEventHandler implements ApplicationEventPublisherAware {
    private ApplicationEventPublisher eventPublisher;
    private final GuildVoiceState guildVoiceState;
    private final AudioChannel audioChannel;

    public AudioEventHandler(ApplicationEventPublisher eventPublisher, GuildVoiceState guildVoiceState) {
        this.eventPublisher = eventPublisher;
        this.guildVoiceState = guildVoiceState;
        this.audioChannel = guildVoiceState.getChannel();
    }

    public MessageEmbed joinVoiceChannel() {
        if (!guildVoiceState.inAudioChannel()) {
            return messageEmbedBuilder("You must be in a voice channel first.", Colors.ADVERT);
        }

        if (audioChannel == null) {
            return messageEmbedBuilder("Failed to join voice channel.", Colors.ADVERT);
        }

        guildVoiceState.getGuild().getAudioManager().setSelfDeafened(true);
        guildVoiceState.getGuild().getAudioManager().openAudioConnection(guildVoiceState.getChannel());

        new Thread(() -> eventPublisher.publishEvent(new VoiceChannelJoinEvent(this, audioChannel.getId(), audioChannel.getName(), audioChannel.getGuild().getName(), audioChannel.getGuild().getIconUrl()))).start();

        return messageEmbedBuilder("Joining: `" + audioChannel.getName() + "`.", Colors.SUCCESS);
    }

    public MessageEmbed leaveVoiceChannel() {
        if (!guildVoiceState.inAudioChannel()) {
            return messageEmbedBuilder("You must be in a voice channel first.", Colors.ADVERT);
        }

        if (audioChannel == null) {
            return messageEmbedBuilder("Failed to leave voice channel.", Colors.ADVERT);
        }

        guildVoiceState.getGuild().getAudioManager().closeAudioConnection();

        new Thread(() -> eventPublisher.publishEvent(new VoiceChannelLeaveEvent(this, audioChannel.getId()))).start();

        return messageEmbedBuilder("Left the voice channel.", Colors.SUCCESS);
    }

    @Override
    public void setApplicationEventPublisher(@NotNull ApplicationEventPublisher applicationEventPublisher) {
        this.eventPublisher = applicationEventPublisher;
    }
}
