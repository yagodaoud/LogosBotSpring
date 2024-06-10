package yagodaoud.com.logos.music.audio.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import yagodaoud.com.logos.music.audio.event.VoiceChannelLeaveEvent;
import yagodaoud.com.logos.webSocket.VoiceChannelWebSocketHandler;

@Component
public class VoiceChannelLeaveListener {

    @Autowired
    private VoiceChannelWebSocketHandler webSocketHandler;

    @EventListener
    public void handleVoiceChannelLeaveEvent(VoiceChannelLeaveEvent event) {
        String message = "{\"channelId\":\"" + event.getChannelId() + "\"," +
                         "\"type\":\"leave\"" + "}";
        webSocketHandler.sendMessageToAll(message);

    }
}
