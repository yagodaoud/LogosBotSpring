package yagodaoud.com.dashboard.controller;

import net.dv8tion.jda.api.JDA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import yagodaoud.com.dashboard.service.GuildService;
import yagodaoud.com.logos.botBuilder.DiscordBotInitializer;
import yagodaoud.com.logos.db.entity.Guild;
import yagodaoud.com.logos.db.repository.GuildRepository;

import java.util.List;

@Controller
@Lazy
public class GuildController {

    @Autowired
    private GuildRepository guildRepository;
    @Autowired
    private GuildService guildService;

    private final JDA discordBot = DiscordBotInitializer.getDiscordBot();

    @GetMapping("/guilds")
    public String guild(Model model) {
        List<Guild> guilds = guildService.findAll();

        model.addAttribute("guilds", guilds);
        return "guilds";
    }

    @PostMapping("/guilds/updateGuilds")
    @ResponseBody
    public List<String> updateGuilds() {
        return guildService.getAllGuilds(discordBot);
    }
}
