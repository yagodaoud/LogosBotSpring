package yagodaoud.com.logos.crypto;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import yagodaoud.com.logos.commands.CommandHandler;

public class CryptoPrice implements CommandHandler {
    @Override
    public void handleCommand(SlashCommandInteractionEvent event, String[] args) {
        event.reply("a").queue();
    }
}
