package yagodaoud.com.logos.help.view;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import yagodaoud.com.logos.tools.Colors;

public class HelpMusicView {
    public static MessageCreateData getMusicView(){

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("Music Commands")
                .setColor(Colors.MUSIC_HELP_VIEW)
                .addField("Join", "Join the voice channel.", true)
                .addField("Play", "Play a song/video/livestream by name or link.", true)
                .addField("Skip", "Skip the current song.", true)
                .addField("Stop", "Stop the music playback.", true)
                .addField("Loop", "Toggle looping of the current song.", true)
                .addField("Leave", "Leave the voice channel.", true)
                .addField("Queue", "Show the upcoming songs.", true)
                .addField("Clear", "Clear the music queue.", true)
                .addField("Shuffle", "Toggle shuffling of the music queue.", true)
                .setFooter("If the queue is stuck, skip or clear the queue and then add a track.\nIf YouTube won't play a track, use SoundCloud instead.");

        MessageCreateBuilder messageBuilder = new MessageCreateBuilder()
                .addEmbeds(builder.build());

        return messageBuilder.build();
    }
}
