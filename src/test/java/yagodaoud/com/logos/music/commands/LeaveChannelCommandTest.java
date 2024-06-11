package yagodaoud.com.logos.music.commands;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yagodaoud.com.logos.commands.CommandRegistryService;
import yagodaoud.com.logos.tools.Colors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static yagodaoud.com.logos.tools.MessageEmbedBuilder.messageEmbedBuilder;

@ExtendWith(MockitoExtension.class)
public class LeaveChannelCommandTest {
    @Mock
    private CommandRegistryService commandRegistry;
    @Mock
    private SlashCommandInteractionEvent event;
    @Mock
    private Member member;
    @Mock
    private Guild guild;
    @Mock
    private GuildVoiceState voiceState;
    @Mock
    private AudioChannelUnion channel;
    @Mock
    private AudioManager audioManager;
    @InjectMocks
    private LeaveChannelCommand command;

    @Nested
    class NestedHandleCommandBlock {

        @BeforeEach
        public void setup() {
            when(event.replyEmbeds(any(MessageEmbed.class))).thenReturn(mock(ReplyCallbackAction.class));
        }

        @Test
        public void handleCommand() {
            when(event.getMember()).thenReturn(member);
            when(event.getMember().getVoiceState()).thenReturn(voiceState);

            when(voiceState.inAudioChannel()).thenReturn(true);
            when(voiceState.getChannel()).thenReturn(channel);
            when(voiceState.getGuild()).thenReturn(guild);
            when(voiceState.getGuild().getAudioManager()).thenReturn(audioManager);
            when(voiceState.getGuild().getAudioManager().isConnected()).thenReturn(true);

            command.handleCommand(event);

            verify(event, times(1)).replyEmbeds(messageEmbedBuilder("Left the voice channel.", Colors.SUCCESS));
        }

        @Test
        public void shouldNotLeaveVoiceChannel() {
            when(event.getMember()).thenReturn(member);
            when(event.getMember().getVoiceState()).thenReturn(voiceState);

            when(voiceState.inAudioChannel()).thenReturn(false);

            command.handleCommand(event);

            verify(event, times(1)).replyEmbeds(messageEmbedBuilder("You must be in a voice channel first.", Colors.ADVERT));
        }

        @Test
        public void shouldFailToLeaveVoiceChannel() {
            when(event.getMember()).thenReturn(member);
            when(event.getMember().getVoiceState()).thenReturn(voiceState);

            when(voiceState.inAudioChannel()).thenReturn(true);
            when(voiceState.getChannel()).thenReturn(null);

            command.handleCommand(event);

            verify(event, times(1)).replyEmbeds(messageEmbedBuilder("Failed to leave voice channel.", Colors.ADVERT));
        }

    }

    @Test
    public void shouldReturnName() {
        assertEquals(command.getName(), "leave");
    }

    @Test
    public void shouldReturnDescription() {
        assertEquals(command.getDescription(), "Leave the voice channel.");
    }

    @Test
    public void shouldReturnNullOptions() {
        assertNull(command.getOptions());
    }
}
