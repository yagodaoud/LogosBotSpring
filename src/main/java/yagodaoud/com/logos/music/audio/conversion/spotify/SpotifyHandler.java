package yagodaoud.com.logos.music.audio.conversion.spotify;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.model_objects.specification.Track;
import yagodaoud.com.logos.music.audio.CustomAudioLoadResultHandler;
import yagodaoud.com.logos.music.audio.GuildMusicManager;
import yagodaoud.com.logos.music.audio.conversion.mirror.ExtendedAudioPlaylist;
import yagodaoud.com.logos.music.audio.conversion.spotify.services.SpotifyApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class SpotifyHandler {

    private final SpotifyApiService spotifyApiService;

    @Autowired
    public SpotifyHandler(SpotifyApiService spotifyApiService) {
        this.spotifyApiService = spotifyApiService;
    }

    public SpotifyAudioPlaylist getPlaylistData(String playlistUrl) {
        List<AudioTrack> spotifyTracks = new ArrayList<>();
        PlaylistTrack[] playlistItems = this.spotifyApiService.getPlaylist(playlistUrl).getItems();

        for (PlaylistTrack playlistTrack : playlistItems) {
            Track track = (Track) playlistTrack.getTrack();
            String artistName = track.getArtists()[0].getName();
            String trackName = track.getName();
            spotifyTracks.add(new SpotifyAudioTrack(new AudioTrackInfo(trackName, artistName, track.getDurationMs().longValue(), trackName, false, track.getUri())));
        }
        return new SpotifyAudioPlaylist("spotify-playlist", spotifyTracks, ExtendedAudioPlaylist.Type.PLAYLIST, playlistUrl, null, null, 0);
    }

    public void handle(AudioPlayerManager audioPlayerManager, GuildMusicManager musicManager, SpotifyAudioPlaylist spotifyAudioPlaylist, boolean forcePlay, CompletableFuture<MessageEmbed> futureMessage, AtomicReference<MessageEmbed> messageContainer) {
        CustomAudioLoadResultHandler loadResultHandlerImplementation = new CustomAudioLoadResultHandler(musicManager, spotifyAudioPlaylist, forcePlay, futureMessage, messageContainer);
        for (AudioTrack track : spotifyAudioPlaylist.getTracks()) {
            audioPlayerManager.loadItemOrdered(musicManager, "ytsearch: " + track.getInfo().title + " " + track.getInfo().author, loadResultHandlerImplementation);
        }

    }
}
