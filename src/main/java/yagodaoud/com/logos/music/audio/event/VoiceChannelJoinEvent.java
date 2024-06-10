package yagodaoud.com.logos.music.audio.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class VoiceChannelJoinEvent extends ApplicationEvent {
    private final String channelId;
    private final String channelName;
    private final String guildName;
    private final String guildId;
    private final String guildIconUrl;

    public VoiceChannelJoinEvent(Object source, String channelId, String channelName, String guildName, String guildId, String guildIconUrl) {
        super(source);
        this.channelId = channelId;
        this.channelName = channelName;
        this.guildName = guildName;
        this.guildId = guildId;
        this.guildIconUrl = guildIconUrl;
    }

}
