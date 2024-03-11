package yagodaoud.com.logos.crypto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class BitcoinPriceTrackerService {
    private final BitcoinPriceTracker bitcoinPriceTracker;
    private final CoinMarketCapApiService coinMarketCapApiService;

    @Autowired
    public BitcoinPriceTrackerService(BitcoinPriceTracker bitcoinPriceTracker, CoinMarketCapApiService coinMarketCapApiService) {
        this.bitcoinPriceTracker = bitcoinPriceTracker;
        this.coinMarketCapApiService = coinMarketCapApiService;
    }

    @Scheduled(fixedRate = 5 * 60000)
    public void trackBitcoinPrice() {
        bitcoinPriceTracker.updateCurrentPrice(coinMarketCapApiService.getCryptoPrice("BTC"));
        bitcoinPriceTracker.updatePriceTrend();
        String message = bitcoinPriceTracker.generateNotificationMessage();

        if (message != null) {
            bitcoinPriceTracker.sendNotificationMessage(message);
        }
    }
}

