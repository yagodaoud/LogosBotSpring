package yagodaoud.com.logos;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import yagodaoud.com.logos.commands.CommandRegistryService;
import yagodaoud.com.logos.db.DbEventHandler;
import yagodaoud.com.logos.db.repository.CommandHistoryRepository;
import yagodaoud.com.logos.db.repository.UserRepository;

@Configuration
public class AppConfig {

    @Bean
    public CommandRegistryService commandRegistryService() {
        return new CommandRegistryService();
    }

    @Bean
    public DbEventHandler dbEventHandler(UserRepository userRepository, CommandHistoryRepository commandHistoryRepository) {
        return new DbEventHandler(userRepository, commandHistoryRepository);
    }
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
