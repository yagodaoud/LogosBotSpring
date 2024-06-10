package yagodaoud.com.logos.music.audio.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class VoiceChannelLeaveEvent extends ApplicationEvent {
    private final String channelId;


    public VoiceChannelLeaveEvent(Object source, String channelId) {
        super(source);
        this.channelId = channelId;
    }
}
