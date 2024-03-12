package yagodaoud.com.logos.crypto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

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
        bitcoinPriceTrackerCommand.updateCurrentPrice(coinMarketCapApiService.getCryptoPrice("BTC"));
        bitcoinPriceTrackerCommand.updatePriceTrend();
        String message = bitcoinPriceTrackerCommand.generateNotificationMessage();

        if (message != null) {
            bitcoinPriceTrackerCommand.sendNotificationMessage(message);
        }
    }
}

