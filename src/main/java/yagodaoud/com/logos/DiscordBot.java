package yagodaoud.com.logos;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import yagodaoud.com.logos.botBuilder.DiscordBotInitializer;
import yagodaoud.com.logos.commands.CommandRegistryService;

@SpringBootApplication
public class DiscordBot {

	public static void main(String[] args) {
		SpringApplication.run(DiscordBot.class, args);
	}

	@Bean
	public DiscordBotInitializer discordBotInitializer(CommandRegistryService commandRegistry, ApplicationContext context) {
		return new DiscordBotInitializer(commandRegistry, discordBotToken(), context);
	}

	@Bean
	public String discordBotToken() {
		return Dotenv.configure().load().get("TOKENDISCORDTEST");
	}
}
