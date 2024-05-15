package yagodaoud.com.logos.crypto.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yagodaoud.com.logos.crypto.alertData.AlertDataPercentage;
import yagodaoud.com.logos.crypto.alertData.AlertDataTracker;
import yagodaoud.com.logos.crypto.commands.BitcoinPercentageAlertCommand;
import yagodaoud.com.logos.crypto.commands.BitcoinPriceTrackerCommand;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class BitcoinPriceTrackerServiceTest {
    @Mock
    private CoinMarketCapApiService apiService;
    @Mock
    private BitcoinPriceTrackerCommand command;
    @InjectMocks
    private BitcoinPriceTrackerService service;

    @Test
    public void sendNotificationMessage() {
        AlertDataTracker alertData = mock(AlertDataTracker.class);
        when(alertData.generateNotificationMessage()).thenReturn("message");

        HashMap<String, Map<Long, AlertDataTracker>> alertDataMap = new HashMap<>();
        alertDataMap.put("1", Collections.singletonMap(123L, alertData));

        when(command.getAlertDataMap()).thenReturn(alertDataMap);
        when(apiService.getCryptoPrice(anyString())).thenReturn("123");

        doNothing().when(alertData).updateCurrentPrice(anyString());
        when(alertData.generateNotificationMessage()).thenReturn("aa");

        service.trackBitcoinPrice();

        assertFalse(alertData.getActive());
    }
}
