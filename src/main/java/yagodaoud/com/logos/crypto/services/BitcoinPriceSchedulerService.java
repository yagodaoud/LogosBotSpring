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

    @Autowired
    public BitcoinPriceSchedulerService(BitcoinPriceSchedulerCommand bitcoinPriceSchedulerCommand) {
        this.bitcoinPriceSchedulerCommand = bitcoinPriceSchedulerCommand;
    }

    @Scheduled(cron = "0 0 0 * * *", zone = "UTC")
    public void scheduleBitcoinPrice() {
        if (bitcoinPriceSchedulerCommand.isActive) {
            bitcoinPriceSchedulerCommand.sendBitcoinPrice(bitcoinPriceSchedulerCommand.channel);
        }
    }
}
