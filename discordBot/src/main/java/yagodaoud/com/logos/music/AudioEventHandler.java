package yagodaoud.com.logos.music;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;

public class AudioEventHandler {

    private final GuildVoiceState guildVoiceState;
    private final AudioChannel audioChannel;

    public AudioEventHandler(GuildVoiceState guildVoiceState) {
        this.guildVoiceState = guildVoiceState;
        this.audioChannel = guildVoiceState.getChannel();
    }

    public String joinVoiceChannel() {
        if (!guildVoiceState.inAudioChannel()) {
            return "You must be in a voice channel first.";
        }

        if (audioChannel == null) {
            return "Failed to join voice channel.";
        }

        guildVoiceState.getGuild().getAudioManager().openAudioConnection(guildVoiceState.getChannel());

        return "Joining: `" + audioChannel.getName() + "`.";
    }

    public String leaveVoiceChannel() {
        if (!guildVoiceState.inAudioChannel()) {
            return "You must be in a voice channel first.";
        }

        if (audioChannel == null) {
            return "Failed to leave voice channel.";
        }

        guildVoiceState.getGuild().getAudioManager().closeAudioConnection();

        return "Left the voice channel.";
    }
}
