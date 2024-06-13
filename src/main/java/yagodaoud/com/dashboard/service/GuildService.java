package yagodaoud.com.dashboard.service;

import net.dv8tion.jda.api.JDA;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import yagodaoud.com.logos.db.entity.Guild;
import yagodaoud.com.logos.db.repository.GuildRepository;

import java.util.List;

@Service
public class GuildService {
    @Autowired
    private GuildRepository guildRepository;

    public List<Guild> findAll() {
        return guildRepository.findAll();
    }

    public List<String> getAllGuilds(JDA bot) {
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
