package yagodaoud.com.logos.crypto.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yagodaoud.com.logos.crypto.alertData.AlertDataPercentage;
import yagodaoud.com.logos.crypto.commands.BitcoinPercentageAlertCommand;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BitcoinPercentageAlertServiceTest {
    @Mock
    private CoinMarketCapApiService apiService;
    @Mock
    private BitcoinPercentageAlertCommand command;
    @InjectMocks
    private BitcoinPercentageAlertService service;

    @Test
    public void sendNotificationMessage() {
        AlertDataPercentage alertData = mock(AlertDataPercentage.class);
        when(alertData.generateNotificationMessage()).thenReturn("message");

        HashMap<String, Map<Long, AlertDataPercentage>> alertDataMap = new HashMap<>();
        alertDataMap.put("1", Collections.singletonMap(123L, alertData));

        when(command.getAlertDataMap()).thenReturn(alertDataMap);
        when(apiService.getCryptoPrice(anyString())).thenReturn("123");

        doNothing().when(alertData).updateCurrentPrice(anyString());
        when(alertData.generateNotificationMessage()).thenReturn("aa");

        service.trackBitcoinPercentage();

        assertFalse(alertData.getActive());
    }
}