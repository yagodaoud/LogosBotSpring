package yagodaoud.com.logos.crypto.commands;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yagodaoud.com.logos.commands.CommandRegistryService;
import yagodaoud.com.logos.crypto.alertData.AlertDataScheduler;
import yagodaoud.com.logos.tools.Colors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static yagodaoud.com.logos.tools.MessageEmbedBuilder.messageEmbedBuilder;

@ExtendWith(MockitoExtension.class)
public class BitcoinPriceSchedulerCommandTest {

    @Mock
    private CommandRegistryService commandRegistry;
    @Mock
    private SlashCommandInteractionEvent event;

    @Mock
    private MessageChannelUnion messageChannelUnion;
    @Mock
    private TextChannel textChannel;
    @Mock
    private User user;

    @InjectMocks
    private BitcoinPriceSchedulerCommand command;

    @Nested
    class NestedHandleCommandBlock {

        @BeforeEach
        public void setup() {
            command.alertDataMap.put(1L, mock(AlertDataScheduler.class));

            when(event.replyEmbeds(any(MessageEmbed.class))).thenReturn(mock(ReplyCallbackAction.class));
        }
        @Test
        public void shouldHandleSchedulerAndSendMessage() {
            when(event.isFromGuild()).thenReturn(true);
            when(textChannel.getIdLong()).thenReturn(123L);
            when(event.getChannel()).thenReturn(messageChannelUnion);
            when(event.getChannel().asTextChannel()).thenReturn(textChannel);

            command.handleCommand(event);

            assertTrue(command.alertDataMap.containsKey(1L));
            verify(event, times(1)).replyEmbeds(messageEmbedBuilder("The daily closing price of Bitcoin will be displayed from now on!", Colors.SUCCESS));
        }

        @Test
        public void shouldHandleAlreadyActiveCommand() {
            when(event.isFromGuild()).thenReturn(true);
            when(textChannel.getIdLong()).thenReturn(123L);
            when(event.getChannel()).thenReturn(messageChannelUnion);
            when(event.getChannel().asTextChannel()).thenReturn(textChannel);

            AlertDataScheduler mockAlertData = mock(AlertDataScheduler.class);
            when(mockAlertData.getActive()).thenReturn(true);
            command.alertDataMap.put(123L, mockAlertData);

            command.handleCommand(event);

            verify(event, times(1)).replyEmbeds(messageEmbedBuilder("The command is already active.", Colors.ADVERT));
        }
    }

    @Test
    public void shouldReturnName() {
        assertEquals(command.getName(), "bitcoin-price-scheduler");
    }

    @Test
    public void shouldReturnDescription() {
        assertEquals(command.getDescription(), "Send the price of Bitcoin at 12:00 AM UTC everyday");
    }

    @Test
    public void shouldReturnNullOptions() {
        assertNull(command.getOptions());
    }
}
