package yagodaoud.com.logos.music.audio;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import yagodaoud.com.logos.tools.Colors;

import static yagodaoud.com.logos.tools.MessageEmbedBuilder.messageEmbedBuilder;

public class AudioEventHandler {

    private final GuildVoiceState guildVoiceState;
    private final AudioChannel audioChannel;

    public AudioEventHandler(GuildVoiceState guildVoiceState) {
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

        return messageEmbedBuilder("Left the voice channel.", Colors.SUCCESS);
    }
}
