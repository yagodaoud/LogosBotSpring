package yagodaoud.com.dashboard.controller;

import net.dv8tion.jda.api.JDA;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import yagodaoud.com.logos.botBuilder.DiscordBotInitializer;

@Controller
@Lazy
public class DashboardController {
    private final JDA discordBot = DiscordBotInitializer.getDiscordBot();

    @GetMapping("/dashboard")
    public String dashboard() {
        return "dashboard";
    }
}

