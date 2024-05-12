package yagodaoud.com.logos.crypto.alertData;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AlertDataPercentageTest {

    private AlertDataPercentage alertData;

    @Mock
    private TextChannel channel;

    @BeforeEach
    public void setup() {
        this.alertData = new AlertDataPercentage(1, channel);
        alertData.updateCurrentPrice("120");
        alertData.setLastPrice(100);
    }

    @Test
    public void shouldReturnActive_WhenSetActiveTrue() {
        alertData.setActive(true);
        assertTrue(alertData.getActive());
    }
    @Test
    public void shouldGeneratePriceVariation() {
        double variation = alertData.priceVariationCalculator();
        assertEquals(20, variation);
    }

    @Test
    public void shouldGenerateMessage_WhenValueFinite() {
        assertNotNull(alertData.generateNotificationMessage());
    }

    @Test
    public void shouldBeInactive_WhenSendMessageInChannel() {
        AlertDataPercentage alertData = mock(AlertDataPercentage.class);
        doNothing().when(alertData).sendNotificationMessage(anyString());
        alertData.sendNotificationMessage(anyString());
        assertFalse(alertData.getActive());
    }

    @Test
    public void shouldStillBeActive_WhenMessageIsNull() {
        alertData.sendNotificationMessage(null);
        assertTrue(alertData.getActive());
    }
}
