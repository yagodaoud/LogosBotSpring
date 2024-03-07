package yagodaoud.com.main;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import yagodaoud.com.main.botBuilder.BotBuilder;

@SpringBootApplication
public class DiscordBot {

	public static void main(String[] args) {
		SpringApplication.run(DiscordBot.class, args);
		String token = Dotenv.configure().load().get("TOKENDISCORD");

		JDA discordBot = BotBuilder.initBot(token);
		BotBuilder.setActivity(discordBot);

	}

}
