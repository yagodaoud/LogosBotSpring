package yagodaoud.com.logos.music;

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent;
import net.dv8tion.jda.api.interactions.commands.OptionType;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import yagodaoud.com.logos.commands.CommandHandlerInterface;

import java.util.List;

public class PlayCommand implements CommandHandlerInterface {
    @Override
    public void handleCommand(SlashCommandInteractionEvent event) {

    }

    @Override
    public String getName() {
        return "play";
    }

    @Override
    public String getDescription() {
        return "Play a song or playlist from YouTube";
    }

    @Override
    public List<OptionData> getOptions() {
        return List.of(new OptionData(OptionType.STRING, "query", "Song name/playlist/link", true));
    }
}
