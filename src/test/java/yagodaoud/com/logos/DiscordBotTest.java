package yagodaoud.com.logos;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import yagodaoud.com.logos.botBuilder.DiscordBotInitializer;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class DiscordBotTest {

    @Autowired
    private DiscordBotInitializer discordBotInitializer;

    @Test
    void test() {
        assertThat(discordBotInitializer).isNotNull();
    }
}
