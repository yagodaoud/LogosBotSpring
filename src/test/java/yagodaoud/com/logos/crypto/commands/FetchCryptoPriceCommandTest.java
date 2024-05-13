package yagodaoud.com.logos.crypto.commands;


import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionMapping;
import net.dv8tion.jda.api.requests.restaction.interactions.ReplyCallbackAction;
import org.json.JSONException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yagodaoud.com.logos.commands.CommandRegistryService;
import yagodaoud.com.logos.crypto.services.CoinMarketCapApiService;
import yagodaoud.com.logos.tools.Colors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static yagodaoud.com.logos.tools.EmbedErrorMessageBuilder.getWrongOptionTypeMessage;
import static yagodaoud.com.logos.tools.MessageEmbedBuilder.messageEmbedBuilder;

@ExtendWith(MockitoExtension.class)
public class FetchCryptoPriceCommandTest {

    @Mock
    private CommandRegistryService commandRegistry;

    @Mock
    private CoinMarketCapApiService coinMarketCapApiService;

    @Mock
    private SlashCommandInteractionEvent event;

    @InjectMocks
    private FetchCryptoPriceCommand command;

    @Nested
    class NestedHandleCommandBlock {

        @BeforeEach
        public void setup() {
            OptionMapping optionMapping = mock(OptionMapping.class);
            when(optionMapping.getAsString()).thenReturn(anyString());
            when(event.getOption("crypto-symbol")).thenReturn(optionMapping);
            when(event.replyEmbeds(any(MessageEmbed.class))).thenReturn(mock(ReplyCallbackAction.class));
        }
        @Test
        public void shouldHandleValidCryptoSymbolOption() {
            String cryptoSymbol = event.getOption("crypto-symbol").getAsString().toUpperCase();
            String cryptoPrice = coinMarketCapApiService.getCryptoPrice(anyString());
            command.handleCommand(event);

            verify(event, times(1)).replyEmbeds(messageEmbedBuilder("The current price of " + cryptoSymbol + " is " + cryptoPrice, Colors.SUCCESS));
        }

        @Test
        public void shouldHandleInvalidCryptoSymbol() {
            when(coinMarketCapApiService.getCryptoPrice(anyString())).thenThrow(JSONException.class);
            command.handleCommand(event);

            verify(event, times(1)).replyEmbeds(getWrongOptionTypeMessage("ticker symbol"));
        }
    }

    @Test
    public void shouldReturnName() {
        assertEquals(command.getName(), "crypto-price");
    }

    @Test
    public void shouldReturnDescription() {
        assertEquals(command.getDescription(), "get the crypto price");
    }

    @Test
    public void shouldReturnOptions() {
        assertEquals(command.getOptions().get(0).getName(), "crypto-symbol");
    }
}
