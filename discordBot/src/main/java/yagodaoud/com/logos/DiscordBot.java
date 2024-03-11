package yagodaoud.com.logos;

import io.github.cdimascio.dotenv.Dotenv;
import net.dv8tion.jda.api.JDA;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import yagodaoud.com.logos.botBuilder.DiscordBotInitializer;

@SpringBootApplication
public class DiscordBot {

	public static void main(String[] args) {
		ApplicationContext context = SpringApplication.run(DiscordBot.class, args);
		String token = Dotenv.configure().load().get("TOKENDISCORDTEST");

		JDA discordBot = DiscordBotInitializer.initBot(token, context);
		DiscordBotInitializer.setActivity(discordBot);
	}

}
