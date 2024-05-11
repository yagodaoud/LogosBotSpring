package yagodaoud.com.logos.crypto;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yagodaoud.com.logos.crypto.alertData.AlertDataTracker;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)

public class AlertDataTrackerTest {

    private AlertDataTracker alertData;
    @Mock
    private TextChannel channel;


    @BeforeEach
    public void setup() {
        this.alertData = new AlertDataTracker(1, channel, "0");
        alertData.updateCurrentPrice("120");
    }

    @Test
    public void shouldReturnActive_WhenSetActiveTrue() {
        alertData.setActive(true);
        assertTrue(alertData.getActive());
    }

    @Test
    public void shouldUpdatePriceTrendToMinusOne_WhenStartPriceEqualOne() {
        alertData.updatePriceTrend(1);
        assertEquals(-1, alertData.getPriceTrend());
    }

    @Test
    public void shouldUpdatePriceTrendToZero_WhenStartPriceGreaterThanOne() {
        alertData.updatePriceTrend(2);
        assertEquals(0, alertData.getPriceTrend());
    }

    @Test
    public void shouldUpdatePriceTrendToOne_WhenStartPriceLessThanOne() {
        alertData.updatePriceTrend(0);
        assertEquals(1, alertData.getPriceTrend());
    }

    @Test
    public void shouldGenerateMessage_WhenCurrentPriceLessThanTargetPriceAndPriceTrendEqualZero() {
        alertData.updatePriceTrend(2);
        alertData.updateCurrentPrice("0");
        assertNotNull(alertData.generateNotificationMessage());
    }

    @Test
    public void shouldGenerateMessage_WhenCurrentPriceGreaterThanTargetPriceAndPriceTrendEqualOne() {
        alertData.updatePriceTrend(0);
        alertData.updateCurrentPrice("2");
        assertNotNull(alertData.generateNotificationMessage());
    }

    @Test
    public void shouldStillBeActive_WhenMessageIsNull() {
        alertData.sendNotificationMessage(null);
        assertTrue(alertData.getActive());
    }
}
