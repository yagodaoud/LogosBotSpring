package yagodaoud.com.logos.db;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import yagodaoud.com.logos.db.entity.User;
import yagodaoud.com.logos.db.repository.UserRepository;

@Component
public class DbEventHandler {

    private final UserRepository userRepository;

    @Autowired
    public DbEventHandler(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void insertUser(Long discordId, String globalName, String guildName) {
        User user = new User();
        user.setDiscordId(discordId);
        user.setGlobalName(globalName);
        user.setGuildName(guildName);
        userRepository.save(user);
    }
}
