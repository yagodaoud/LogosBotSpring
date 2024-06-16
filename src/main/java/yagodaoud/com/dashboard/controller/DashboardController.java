package yagodaoud.com.dashboard.controller;

import net.dv8tion.jda.api.JDA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import yagodaoud.com.dashboard.service.CommandHistoryService;
import yagodaoud.com.dashboard.service.GuildService;
import yagodaoud.com.dashboard.service.VoiceService;
import yagodaoud.com.logos.botBuilder.DiscordBotInitializer;

@Controller
@Lazy
public class DashboardController {
    private final JDA discordBot = DiscordBotInitializer.getDiscordBot();

    @Autowired
    private GuildService guildService;

    @Autowired
    private VoiceService voiceService;

    @Autowired
    private CommandHistoryService commandHistoryService;

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        long guildTotal = guildService.getTotalGuilds();
        int voiceTotal = voiceService.countAllConnectedChannels(discordBot);
        long commandTotal = commandHistoryService.getTotalCommands();

        model.addAttribute("guildTotal", guildTotal);
        model.addAttribute("voiceTotal", voiceTotal);
        model.addAttribute("commandTotal", commandTotal);
        return "dashboard";
    }
}

