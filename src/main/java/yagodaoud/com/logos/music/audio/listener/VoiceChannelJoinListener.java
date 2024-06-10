package yagodaoud.com.logos.music.audio.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import yagodaoud.com.logos.music.audio.event.VoiceChannelJoinEvent;
import yagodaoud.com.logos.webSocket.VoiceChannelWebSocketHandler;

@Component
public class VoiceChannelJoinListener {

    @Autowired
    private VoiceChannelWebSocketHandler webSocketHandler;

    @EventListener
    public void handleVoiceChannelJoinEvent(VoiceChannelJoinEvent event) {
        String message = "{\"channelId\":\"" + event.getChannelId() + "\"," +
                "\"channelName\":\"" + event.getChannelName() + "\"," +
                "\"guildName\":\"" + event.getGuildName() + "\"," +
                "\"type\":\"join\"," +
                "\"guildIconUrl\":\"" + event.getGuildIconUrl() + "\"}";

        webSocketHandler.sendMessageToAll(message);

    }
}
