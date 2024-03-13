package yagodaoud.com.logos.music;

import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.concrete.VoiceChannel;
import net.dv8tion.jda.api.entities.channel.middleman.AudioChannel;
import net.dv8tion.jda.api.events.Event;
import org.springframework.beans.factory.annotation.Autowired;

public class PlayerManager {

    private Member member;
    private GuildVoiceState voiceState;
    private TrackService trackService;

    @Autowired
    public PlayerManager(Member member, GuildVoiceState voiceState, TrackService trackService) {
        this.member = member;
        this.voiceState = voiceState;
        this.trackService = trackService;
    }

    public String handle(TextChannel channel, String url, Event event) {
        AudioChannel audioChannel = voiceState.getChannel();

        if (!(audioChannel instanceof VoiceChannel)) {
            return ("You must be in a voice channel to use this command.");
        }

        VoiceChannel voiceChannel = (VoiceChannel) audioChannel;
        GuildVoiceState memberVoiceState = member.getVoiceState();

        if (!memberVoiceState.getChannel().equals(voiceChannel)) {
            return ("You must be in the same voice channel as me.");
        }

//        joinVoiceChannelByPlayCommand(voiceState, audioChannel.getGuild(), (SlashCommandInteractionEvent) event);

        String search = String.join(" ", url);
        String link = "ytsearch:" + search;
        trackService.loadTrack(voiceState.getGuild(), link);
        return "asd";
    }
}
