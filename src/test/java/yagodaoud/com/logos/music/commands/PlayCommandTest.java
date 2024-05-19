package yagodaoud.com.logos.music.commands;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.entities.GuildVoiceState;
import net.dv8tion.jda.api.entities.Member;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.AudioChannelUnion;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.managers.AudioManager;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yagodaoud.com.logos.commands.CommandRegistryService;
import yagodaoud.com.logos.music.audio.PlayerManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(MockitoExtension.class)
public class PlayCommandTest {
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
    private AudioChannelUnion audioChannel;
    @Mock
    private TextChannel textChannel;
    @Mock
    private MessageChannelUnion messageChannelUnion;
    @Mock
    private AudioManager audioManager;
    @Mock
    private PlayerManager playerManager;
    @Mock
    private AudioPlayerManager audioPlayerManager;
    @Mock
    private ReplyCallbackAction callbackAction;
    @InjectMocks
    private PlayCommand command;

//    @Nested
//    class NestedHandleCommandBlock {
//        @BeforeEach
//        public void setup() {
//            OptionMapping optionMapping = mock(OptionMapping.class);
//            when(optionMapping.getAsString()).thenReturn("test");
//            when(event.getOption("provider")).thenReturn(null);
//            when(event.getOption("query")).thenReturn(optionMapping);
//
//            when(event.deferReply()).thenReturn(callbackAction);
//            doNothing().when(callbackAction).queue();
//
//            when(event.replyEmbeds(any(MessageEmbed.class))).thenReturn(callbackAction);
//        }
//
//        @Test
//        public void handleCommand() {
//            when(event.getMember()).thenReturn(member);
//            when(event.getMember().getVoiceState()).thenReturn(voiceState);
//            when(event.getChannel()).thenReturn(messageChannelUnion);
//            when(messageChannelUnion.asTextChannel()).thenReturn(textChannel);
//            when(voiceState.inAudioChannel()).thenReturn(true);
//            when(voiceState.getChannel()).thenReturn(audioChannel);
//            when(voiceState.getGuild()).thenReturn(guild);
//            when(voiceState.getGuild().getAudioManager()).thenReturn(audioManager);
//
//            when(voiceState.getChannel().getName()).thenReturn("channel");
//
//            CompletableFuture<MessageEmbed> futureMessage = CompletableFuture.completedFuture(mock(MessageEmbed.class));
//            when(playerManager.loadAndPlay(any(TextChannel.class), any(GuildVoiceState.class), anyString(), anyString(), anyBoolean())).thenReturn(futureMessage);
//
//            command.handleCommand(event);
//
//            verify(event, times(1)).replyEmbeds(messageEmbedBuilder("Joining: `" + "channel" + "`.", Colors.SUCCESS));
//        }
//
//        @Test
//        public void shouldNotJoinVoiceChannel() {
//            when(event.getMember()).thenReturn(member);
//            when(event.getMember().getVoiceState()).thenReturn(voiceState);
//
//            when(voiceState.inAudioChannel()).thenReturn(false);
//
//            command.handleCommand(event);
//
//            verify(event, times(1)).replyEmbeds(messageEmbedBuilder("You must be in a voice channel first.", Colors.ADVERT));
//        }
//
//        @Test
//        public void shouldFailToJoinVoiceChannel() {
//            when(event.getMember()).thenReturn(member);
//            when(event.getMember().getVoiceState()).thenReturn(voiceState);
//
//            when(voiceState.inAudioChannel()).thenReturn(true);
//            when(voiceState.getChannel()).thenReturn(null);
//
//            command.handleCommand(event);
//
//            verify(event, times(1)).replyEmbeds(messageEmbedBuilder("Failed to join voice channel.", Colors.ADVERT));
//        }
//    }

    @Test
    public void shouldReturnName() {
        assertEquals(command.getName(), "play");
    }

    @Test
    public void shouldReturnDescription() {
        assertEquals(command.getDescription(), "Play a song or playlist from YouTube.");
    }

    @Test
    public void shouldReturnNullOptions() {
        List<OptionData> options = command.getOptions();
        assertTrue(options.stream().anyMatch(o -> o.getName().equals("query") || o.getName().equals("provider")));
    }
}
