package yagodaoud.com.logos.crypto.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import yagodaoud.com.logos.crypto.commands.BitcoinPriceTrackerCommand;

@Configuration
@EnableScheduling
public class BitcoinPriceTrackerService {
    private final BitcoinPriceTrackerCommand bitcoinPriceTrackerCommand;
    private final CoinMarketCapApiService coinMarketCapApiService;

    @Autowired
    public BitcoinPriceTrackerService(BitcoinPriceTrackerCommand bitcoinPriceTrackerCommand, CoinMarketCapApiService coinMarketCapApiService) {
        this.bitcoinPriceTrackerCommand = bitcoinPriceTrackerCommand;
        this.coinMarketCapApiService = coinMarketCapApiService;
    }

    @Scheduled(fixedRate = 3 * 60000)
    public void trackBitcoinPrice() {
        String btcPrice = coinMarketCapApiService.getCryptoPrice("BTC");
        bitcoinPriceTrackerCommand.alertDataMap.forEach((userId, userAlertData) -> {
            userAlertData.values().forEach(alertData -> {
                alertData.updateCurrentPrice(btcPrice);
                String message = alertData.generateNotificationMessage();
                if (message != null) {
                    alertData.sendNotificationMessage(message);
                    alertData.setActive(false);
                }
            });
        });
    }
}

