package yagodaoud.com.logos.crypto.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import yagodaoud.com.logos.crypto.alertData.AlertDataScheduler;
import yagodaoud.com.logos.crypto.commands.BitcoinPriceSchedulerCommand;

import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class BitcoinPriceSchedulerServiceTest {
    @Mock
    private CoinMarketCapApiService apiService;
    @Mock
    private BitcoinPriceSchedulerCommand command;
    @InjectMocks
    private BitcoinPriceSchedulerService service;

    @Test
    public void sendNotificationMessage() {
        AlertDataScheduler alertData = mock(AlertDataScheduler.class);

        HashMap<Long, AlertDataScheduler> alertDataMap = new HashMap<>();
        alertDataMap.put(123L, alertData);

        when(command.getAlertDataMap()).thenReturn(alertDataMap);
        when(apiService.getCryptoPrice(anyString())).thenReturn("123");

        service.scheduleBitcoinPrice();

        verify(alertData).sendBitcoinPrice("123");
    }
}
