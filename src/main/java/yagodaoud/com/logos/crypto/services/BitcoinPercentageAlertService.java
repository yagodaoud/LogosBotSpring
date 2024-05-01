package yagodaoud.com.logos.crypto.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import yagodaoud.com.logos.crypto.commands.BitcoinPercentageAlertCommand;

@Configuration
@EnableScheduling
public class BitcoinPercentageAlertService {

    private final BitcoinPercentageAlertCommand bitcoinPercentageAlertCommand;
    private final CoinMarketCapApiService coinMarketCapApiService;

    @Autowired
    public BitcoinPercentageAlertService(BitcoinPercentageAlertCommand bitcoinPercentageAlertCommand, CoinMarketCapApiService coinMarketCapApiService) {
        this.bitcoinPercentageAlertCommand = bitcoinPercentageAlertCommand;
        this.coinMarketCapApiService = coinMarketCapApiService;
    }

    @Scheduled(fixedRate = 60 * 60000, initialDelay = 0)
    public void trackBitcoinPercentage() {
        bitcoinPercentageAlertCommand.alertDataMap.forEach((channelId, alertData) -> {
            alertData.updateCurrentPrice(coinMarketCapApiService.getCryptoPrice("BTC"));
            String message = alertData.generateNotificationMessage();
            if (message != null) {
                alertData.sendNotificationMessage(message);
                alertData.setActive(false);
            }
        });
    }
}
