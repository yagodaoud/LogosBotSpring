package yagodaoud.com.logos.music.spotify.handlers;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.model_objects.specification.Track;
import yagodaoud.com.logos.music.audio.CustomAudioLoadResultHandler;
import yagodaoud.com.logos.music.audio.GuildMusicManager;
import yagodaoud.com.logos.music.spotify.services.SpotifyApiService;

import java.util.TreeMap;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class SpotifyHandler {

    private final SpotifyApiService spotifyApiService;

    @Autowired
    public SpotifyHandler(SpotifyApiService spotifyApiService) {
        this.spotifyApiService = spotifyApiService;
    }

    public TreeMap<String, String> getPlaylistData(String playlistUrl) {
        TreeMap<String, String> tracks = new TreeMap<>();
        PlaylistTrack[] playlistItems = this.spotifyApiService.getPlaylist(playlistUrl).getItems();

        for (PlaylistTrack playlistTrack : playlistItems) {
            Track track = (Track) playlistTrack.getTrack();
            String artistName = track.getArtists()[0].getName();
            String trackName = track.getName();
            System.out.println(artistName + " " + trackName);
            tracks.put(artistName, trackName);
        }
        return tracks;
    }

    public void handle(AudioPlayerManager audioPlayerManager, GuildMusicManager musicManager, String query, boolean forcePlay, CompletableFuture<MessageEmbed> futureMessage, AtomicReference<MessageEmbed> messageContainer) {
        String[] elements = query.split("\\\\");

        for (String element : elements) {
            CustomAudioLoadResultHandler loadResultHandlerImplementation = new CustomAudioLoadResultHandler(musicManager, element, forcePlay, futureMessage, messageContainer);
            audioPlayerManager.loadItemOrdered(musicManager, element, loadResultHandlerImplementation);
        }

    }
}
