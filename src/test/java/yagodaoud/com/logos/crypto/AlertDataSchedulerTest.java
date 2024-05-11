package yagodaoud.com.logos.crypto;

import net.dv8tion.jda.api.entities.channel.concrete.TextChannel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yagodaoud.com.logos.crypto.alertData.AlertDataPercentage;
import yagodaoud.com.logos.crypto.alertData.AlertDataScheduler;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)

public class AlertDataSchedulerTest {

    private AlertDataScheduler alertData;

    @Mock
    private TextChannel channel;

    @BeforeEach
    public void setup() {
        this.alertData = new AlertDataScheduler(channel);
    }

    @Test
    public void shouldReturnActive_WhenSetActiveTrue() {
        alertData.setActive(true);
        assertTrue(alertData.getActive());
    }

    @Test
    public void shouldBeInactive_WhenSendMessageInChannel() {
        AlertDataPercentage alertData = mock(AlertDataPercentage.class);
        doNothing().when(alertData).sendNotificationMessage(anyString());
        alertData.sendNotificationMessage(anyString());
        assertFalse(alertData.getActive());
    }

    @Test
    public void shouldBeInactive_WhenInactivelySendPrice() {
        alertData.setActive(false);
        alertData.sendBitcoinPrice("");
        assertFalse(alertData.getActive());
    }
}
