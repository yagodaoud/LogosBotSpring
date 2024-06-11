package yagodaoud.com.dashboard.controller;

import net.dv8tion.jda.api.JDA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import yagodaoud.com.logos.botBuilder.DiscordBotInitializer;
import yagodaoud.com.logos.db.entity.Guild;
import yagodaoud.com.logos.db.repository.GuildRepository;

import java.util.List;

@Controller
@Lazy
public class GuildController {

    @Autowired
    private GuildRepository guildRepository;

    private final JDA discordBot = DiscordBotInitializer.getDiscordBot();

    @GetMapping("/guilds")
    public String guild(Model model) {
        List<Guild> guilds = guildRepository.findAll();
        model.addAttribute("guilds", guilds);
        return "guilds";
    }

    @PostMapping("/guilds/updateGuilds")
    @ResponseBody
    public List<String> updateGuilds() {
        return getAllGuilds(discordBot);
    }

    private List<String> getAllGuilds(JDA bot) {
        List<net.dv8tion.jda.api.entities.Guild> guilds = bot.getGuilds();

        guildRepository.deleteAll();

        guilds.forEach(guild -> {
            yagodaoud.com.logos.db.entity.Guild guildEntity = new yagodaoud.com.logos.db.entity.Guild();
            guildEntity.setGuildId(guild.getId());
            guildEntity.setGuildName(guild.getName());
            guildEntity.setGuildDescription(guild.getDescription());
            guildEntity.setGuildIconUrl(guild.getIconUrl());
            guildEntity.setGuildMemberCount(guild.getMemberCount());

            guildRepository.save(guildEntity);
        });

        return guilds.stream().map(net.dv8tion.jda.api.entities.Guild::getName).toList();
    }
}
