package yagodaoud.com.logos.botBuilder;

import jakarta.annotation.PostConstruct;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.requests.GatewayIntent;
import net.dv8tion.jda.api.utils.ChunkingFilter;
import net.dv8tion.jda.api.utils.MemberCachePolicy;
import net.dv8tion.jda.api.utils.cache.CacheFlag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import yagodaoud.com.logos.commands.CommandRegistryService;
import yagodaoud.com.logos.listeners.BotCommandsListener;
import yagodaoud.com.logos.listeners.LoadCommandsListener;

import java.util.EnumSet;

public class DiscordBotInitializer {

    private final CommandRegistryService commandRegistry;
    private final String token;
    private final ApplicationContext context;

    @Autowired
    public DiscordBotInitializer(CommandRegistryService commandRegistry, String token, ApplicationContext context) {
        this.commandRegistry = commandRegistry;
        this.token = token;
        this.context = context;
    }

    @PostConstruct
    public void initializeBot() {
        JDABuilder builder = JDABuilder.createDefault(token)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .setChunkingFilter(ChunkingFilter.ALL)
                .enableCache(CacheFlag.VOICE_STATE)
                .setAutoReconnect(true);

        disableCache(builder);
        addGatewayIntents(builder);
        configureEventListeners(builder, context);

        JDA discordBot = builder.build();
        setActivity(discordBot);
    }

    private void disableCache(JDABuilder builder) {
        builder.disableCache(EnumSet.of(
                CacheFlag.CLIENT_STATUS,
                CacheFlag.ACTIVITY,
                CacheFlag.EMOJI,
                CacheFlag.STICKER,
                CacheFlag.SCHEDULED_EVENTS));
    }

    private static void configureEventListeners(JDABuilder builder, ApplicationContext context) {
        LoadCommandsListener loadCommandsListener = context.getBean(LoadCommandsListener.class);
        BotCommandsListener botCommandsListener = context.getBean(BotCommandsListener.class);
        builder.addEventListeners(loadCommandsListener, botCommandsListener);
    }

    private void addGatewayIntents(JDABuilder builder) {
        builder.enableIntents(
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_VOICE_STATES
        );
    }

    private void setActivity(JDA bot) {
        bot.getPresence().setActivity(Activity.listening("/help"));
    }
}
