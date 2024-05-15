package yagodaoud.com.logos.crypto.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import yagodaoud.com.logos.crypto.commands.BitcoinPriceSchedulerCommand;

@Configuration
@EnableScheduling
public class BitcoinPriceSchedulerService {
    private final BitcoinPriceSchedulerCommand bitcoinPriceSchedulerCommand;
    private final CoinMarketCapApiService coinMarketCapApiService;


    @Autowired
    public BitcoinPriceSchedulerService(BitcoinPriceSchedulerCommand bitcoinPriceSchedulerCommand, CoinMarketCapApiService coinMarketCapApiService) {
        this.bitcoinPriceSchedulerCommand = bitcoinPriceSchedulerCommand;
        this.coinMarketCapApiService = coinMarketCapApiService;
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "UTC")
    public void scheduleBitcoinPrice() {
        String btcPrice = coinMarketCapApiService.getCryptoPrice("BTC");
        bitcoinPriceSchedulerCommand.getAlertDataMap().forEach((channelId, alertData) -> {
                alertData.sendBitcoinPrice(btcPrice);
        });
    }
}
