package yagodaoud.com.logos.crypto;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
@EnableScheduling
public class BitcoinPriceSchedulerService {
    private final BitcoinPriceSchedulerCommand bitcoinPriceSchedulerCommand;

    @Autowired
    public BitcoinPriceSchedulerService(BitcoinPriceSchedulerCommand bitcoinPriceSchedulerCommand) {
        this.bitcoinPriceSchedulerCommand = bitcoinPriceSchedulerCommand;
    }

    @Scheduled(cron = "40 58 22 * * *", zone = "GMT-3")
    public void scheduleBitcoinPrice() {
        System.out.println("BitcoinPriceSchedulerCommand instance: " + bitcoinPriceSchedulerCommand);
        if (bitcoinPriceSchedulerCommand.isActive) {
            bitcoinPriceSchedulerCommand.sendBitcoinPrice(bitcoinPriceSchedulerCommand.textChannel);
        }
    }
}
