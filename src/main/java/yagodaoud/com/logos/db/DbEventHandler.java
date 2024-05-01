package yagodaoud.com.logos.db;

import net.dv8tion.jda.api.entities.Guild;
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import yagodaoud.com.logos.db.entity.CommandHistory;
import yagodaoud.com.logos.db.entity.User;
import yagodaoud.com.logos.db.repository.CommandHistoryRepository;
import yagodaoud.com.logos.db.repository.UserRepository;

import java.time.LocalDateTime;

@Service
public class DbEventHandler {

    private final UserRepository userRepository;
    private final CommandHistoryRepository commandHistoryRepository;

    @Autowired
    public DbEventHandler(UserRepository userRepository, CommandHistoryRepository commandHistoryRepository) {
        this.userRepository = userRepository;
        this.commandHistoryRepository = commandHistoryRepository;
    }

    @Async
    public void insertDataAsync(SlashCommandInteractionEvent event) {
        net.dv8tion.jda.api.entities.User user = event.getUser();
        Guild guild = event.getGuild();
        insertUserAsync(user);
        String commandOption = null;
        if (event.getOptions().size() > 0) {
            commandOption = event.getOptions().get(0).getAsString();
        }
        insertCommandHistoryAsync(user, guild, event.getName(), commandOption);
    }

    @Async
    public void insertUserAsync(net.dv8tion.jda.api.entities.User eventUser) {
        if (userRepository.findByDiscordId(eventUser.getIdLong()) == null) {
            User user = new User();
            user.setDiscordId(eventUser.getIdLong());
            user.setGlobalName(eventUser.getGlobalName());
            user.setGuildName(eventUser.getName());
            user.setDateAdded(LocalDateTime.now());
            userRepository.save(user);
        }
    }

    @Async
    public void insertCommandHistoryAsync(net.dv8tion.jda.api.entities.User eventUser, Guild eventGuild, String commandName, String commandOption) {
        CommandHistory commandHistory = new CommandHistory();
        User user = userRepository.findByDiscordId(eventUser.getIdLong());
        commandHistory.setUser(user);
        commandHistory.setUserName(user.getGuildName());
        commandHistory.setGuildId(eventGuild.getIdLong());
        commandHistory.setGuildName(eventGuild.getName());
        commandHistory.setCommandName(commandName);
        commandHistory.setDateAdded(LocalDateTime.now());
        commandHistory.setCommandOption(commandOption);
        commandHistoryRepository.save(commandHistory);
    }
}
