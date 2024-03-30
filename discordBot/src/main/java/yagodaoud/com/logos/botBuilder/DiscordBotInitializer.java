package yagodaoud.com.logos.botBuilder;

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
import yagodaoud.com.logos.crypto.commands.BitcoinPercentageAlertCommand;
import yagodaoud.com.logos.crypto.commands.BitcoinPriceSchedulerCommand;
import yagodaoud.com.logos.crypto.commands.BitcoinPriceTrackerCommand;
import yagodaoud.com.logos.crypto.commands.FetchCryptoPriceCommand;
import yagodaoud.com.logos.listeners.BotCommandsListener;
import yagodaoud.com.logos.listeners.LoadCommandsListener;
import yagodaoud.com.logos.music.commands.*;

import java.util.EnumSet;

public class DiscordBotInitializer {

    private final CommandRegistryService commandRegistry;

    @Autowired
    public DiscordBotInitializer(CommandRegistryService commandRegistry) {
        this.commandRegistry = commandRegistry;
    }

    public static JDA initBot(String token,  ApplicationContext context) {

        JDABuilder builder = JDABuilder.createDefault(token)
                .setMemberCachePolicy(MemberCachePolicy.ALL)
                .disableCache(EnumSet.of(
                        CacheFlag.CLIENT_STATUS,
                        CacheFlag.ACTIVITY,
                        CacheFlag.EMOJI,
                        CacheFlag.STICKER,
                        CacheFlag.SCHEDULED_EVENTS
                ))
                .setChunkingFilter(ChunkingFilter.ALL)
                .enableCache(CacheFlag.VOICE_STATE);

        disableCache(builder);
        addGatewayIntents(builder);
        configureEventListeners(builder, context);

        return builder.build();
    }

    private static void disableCache(JDABuilder builder) {
        builder.disableCache((EnumSet.of(
                CacheFlag.CLIENT_STATUS,
                CacheFlag.ACTIVITY,
                CacheFlag.EMOJI,
                CacheFlag.STICKER,
                CacheFlag.SCHEDULED_EVENTS)));
    }

    private static void configureEventListeners(JDABuilder builder, ApplicationContext context) {
        CommandRegistryService commandRegistry = context.getBean(CommandRegistryService.class);
        context.getBean(FetchCryptoPriceCommand.class);
        context.getBean(BitcoinPriceSchedulerCommand.class);
        context.getBean(BitcoinPriceTrackerCommand.class);
        context.getBean(BitcoinPercentageAlertCommand.class);
        context.getBean(JoinChannelCommand.class);
        context.getBean(LeaveChannelCommand.class);
        context.getBean(PlayCommand.class);
        context.getBean(VolumeCommand.class);
        context.getBean(SkipCommand.class);
        context.getBean(NowPlayingCommand.class);
        context.getBean(QueueCommand.class);
        context.getBean(StopCommand.class);
        context.getBean(ResumeCommand.class);
        builder.addEventListeners(new LoadCommandsListener(commandRegistry), new BotCommandsListener(commandRegistry));
    }

    private static void addGatewayIntents(JDABuilder builder) {
        builder.enableIntents(
                GatewayIntent.GUILD_MEMBERS,
                GatewayIntent.GUILD_MESSAGES,
                GatewayIntent.GUILD_PRESENCES,
                GatewayIntent.GUILD_VOICE_STATES
        );
    }

    public static void setActivity(JDA bot) {
        bot.getPresence().setActivity(Activity.listening("/help"));
    }
}
