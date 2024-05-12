package yagodaoud.com.logos.crypto.commands;

import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import net.dv8tion.jda.api.entities.channel.unions.MessageChannelUnion;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yagodaoud.com.logos.commands.CommandRegistryService;
import yagodaoud.com.logos.crypto.alertData.AlertDataPercentage;
import yagodaoud.com.logos.crypto.alertData.AlertDataTracker;
import yagodaoud.com.logos.tools.Colors;

import java.text.NumberFormat;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static yagodaoud.com.logos.tools.EmbedErrorMessageBuilder.getWrongOptionTypeMessage;
import static yagodaoud.com.logos.tools.MessageEmbedBuilder.messageEmbedBuilder;

@ExtendWith(MockitoExtension.class)
public class BitcoinPriceTrackerCommandTest {

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
    private BitcoinPriceTrackerCommand command;

    @Nested
    class NestedHandleCommandBlock {

        @BeforeEach
        public void setup() {
            textChannel = mock(TextChannel.class);
            event = mock(SlashCommandInteractionEvent.class);
            Map<Long, AlertDataTracker> alertDataTrackerMap = new HashMap<>(Collections.singletonMap(123L, mock(AlertDataTracker.class)));
            command.alertDataMap.put("1", alertDataTrackerMap);

            OptionMapping optionMapping = mock(OptionMapping.class);
            when(optionMapping.getAsDouble()).thenReturn(1.0);
            when(event.getOption("target-price")).thenReturn(optionMapping);
            when(event.replyEmbeds(any(MessageEmbed.class))).thenReturn(mock(ReplyCallbackAction.class));
        }
        @Test
        public void shouldHandleValidPercentageOption() {
            when(event.isFromGuild()).thenReturn(true);
            when(textChannel.getIdLong()).thenReturn(123L);
            when(event.getUser()).thenReturn(user);
            when(event.getUser().getId()).thenReturn("123");
            when(event.getChannel()).thenReturn(messageChannelUnion);
            when(event.getChannel().asTextChannel()).thenReturn(textChannel);

            command.handleCommand(event);

            assertTrue(command.alertDataMap.containsKey("1"));
            verify(event, times(1)).replyEmbeds(messageEmbedBuilder("Tracking bitcoin price when it reaches " + NumberFormat.getCurrencyInstance(Locale.US).format(1.0), Colors.SUCCESS));
        }

        @Test
        public void shouldHandleInvalidPercentageOption() {
            when(event.getOption("target-price").getAsDouble()).thenThrow(NumberFormatException.class);
            command.handleCommand(event);

            verify(event, times(1)).replyEmbeds(getWrongOptionTypeMessage("number"));
        }

        @Test
        public void shouldHandleAlreadyActiveCommand() {
            when(event.isFromGuild()).thenReturn(true);
            when(textChannel.getIdLong()).thenReturn(123L);
            when(event.getUser()).thenReturn(user);
            when(event.getUser().getId()).thenReturn("123");
            when(event.getChannel()).thenReturn(messageChannelUnion);
            when(event.getChannel().asTextChannel()).thenReturn(textChannel);

            AlertDataTracker mockAlertData = mock(AlertDataTracker.class);
            when(mockAlertData.getActive()).thenReturn(true);
            Map<Long, AlertDataTracker> userAlertData = new HashMap<>();
            userAlertData.put(123L, mockAlertData);
            command.alertDataMap.put("123", userAlertData);

            command.handleCommand(event);

            verify(event, times(1)).replyEmbeds(messageEmbedBuilder("The command is already active.", Colors.ADVERT));
        }
    }

    @Test
    public void shouldReturnName() {
        assertEquals(command.getName(), "bitcoin-price-tracker");
    }

    @Test
    public void shouldReturnDescription() {
        assertEquals(command.getDescription(), "If the value is reached, the bot will send a notification");
    }

    @Test
    public void shouldReturnOptions() {
        assertEquals(command.getOptions().get(0).getName(), "target-price");
    }
}
