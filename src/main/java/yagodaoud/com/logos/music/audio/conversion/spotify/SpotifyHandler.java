package yagodaoud.com.logos.music.audio.conversion.spotify;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;
import yagodaoud.com.logos.music.audio.CustomAudioLoadResultHandler;
import yagodaoud.com.logos.music.audio.GuildMusicManager;
import yagodaoud.com.logos.music.audio.conversion.mirror.ExtendedAudioPlaylist;
import yagodaoud.com.logos.music.audio.conversion.spotify.services.SpotifyApiService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

@Component
public class SpotifyHandler {

    private final SpotifyApiService spotifyApiService;

    @Autowired
    public SpotifyHandler(SpotifyApiService spotifyApiService) {
        this.spotifyApiService = spotifyApiService;
    }

    public SpotifyAudioObject getSpotifyObject(String url) {
        if (url.contains("playlist")) {
            return getPlaylistData(url);
        }
        if (url.contains("track")) {
            return getTrackData(url);
        }
        if (url.contains("album")) {
            return getAlbumData(url);
        }
        return getArtistData(url);
    }

    private SpotifyAudioObject getTrackData(String trackUrl) {
        List<AudioTrack> spotifyTracks = new ArrayList<>();
        Track track = spotifyApiService.getTrack(trackUrl);
        spotifyTracks.add(new SpotifyAudioTrack(new AudioTrackInfo(track.getName(), track.getArtists()[0].getName(),track.getDurationMs().longValue(), track.getName(), false, track.getUri())));
        return new SpotifyAudioPlaylist("spotify-track", spotifyTracks, ExtendedAudioPlaylist.Type.PLAYLIST, trackUrl, null, null, 0);
    }

    public SpotifyAudioObject getPlaylistData(String playlistUrl) {
        List<AudioTrack> spotifyTracks = new ArrayList<>();
        PlaylistTrack[] playlistItems = spotifyApiService.getPlaylist(playlistUrl).getItems();

        for (PlaylistTrack playlistTrack : playlistItems) {
            Track track = (Track) playlistTrack.getTrack();
            String artistName = track.getArtists()[0].getName();
            String trackName = track.getName();
            spotifyTracks.add(new SpotifyAudioTrack(new AudioTrackInfo(trackName, artistName, track.getDurationMs().longValue(), trackName, false, track.getUri())));
        }
        return new SpotifyAudioPlaylist("spotify-playlist", spotifyTracks, ExtendedAudioPlaylist.Type.PLAYLIST, playlistUrl, null, null, 0);
    }

    private SpotifyAudioObject getArtistData(String artistUrl) {
        List<AudioTrack> spotifyTracks = new ArrayList<>();
        Track[] tracks = spotifyApiService.getArtist(artistUrl);

        for (Track artistTrack : tracks) {
            String artistName = artistTrack.getArtists()[0].getName();
            String trackName = artistTrack.getName();
            spotifyTracks.add(new SpotifyAudioTrack(new AudioTrackInfo(trackName, artistName, artistTrack.getDurationMs().longValue(), trackName, false, artistTrack.getUri())));
        }
        return new SpotifyAudioPlaylist("spotify-playlist", spotifyTracks, ExtendedAudioPlaylist.Type.PLAYLIST, artistUrl, null, null, 0);
    }

    private SpotifyAudioObject getAlbumData(String albumUrl) {
        List<AudioTrack> spotifyTracks = new ArrayList<>();
        TrackSimplified[] albumTracks = spotifyApiService.getAlbum(albumUrl).getItems();

        for (TrackSimplified albumTrack : albumTracks) {
            String artistName = albumTrack.getArtists()[0].getName();
            String trackName = albumTrack.getName();
            spotifyTracks.add(new SpotifyAudioTrack(new AudioTrackInfo(trackName, artistName, albumTrack.getDurationMs().longValue(), trackName, false, albumTrack.getUri())));
        }
        return new SpotifyAudioPlaylist("spotify-playlist", spotifyTracks, ExtendedAudioPlaylist.Type.PLAYLIST, albumUrl, null, null, 0);
    }

    public void handle(AudioPlayerManager audioPlayerManager, GuildMusicManager musicManager, SpotifyAudioObject spotifyAudioObject, boolean forcePlay, CompletableFuture<MessageEmbed> futureMessage, AtomicReference<MessageEmbed> messageContainer) {
        CustomAudioLoadResultHandler loadResultHandlerImplementation = new CustomAudioLoadResultHandler(musicManager, spotifyAudioObject, forcePlay, futureMessage, messageContainer);
        for (AudioTrack track : spotifyAudioObject.getSpotifyTracks()) {
            audioPlayerManager.loadItemOrdered(musicManager, "ytsearch: " + track.getInfo().title + " " + track.getInfo().author, loadResultHandlerImplementation);
        }
    }
}
