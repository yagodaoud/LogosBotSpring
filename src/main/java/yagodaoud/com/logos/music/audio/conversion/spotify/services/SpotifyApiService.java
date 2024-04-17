package yagodaoud.com.logos.music.audio.conversion.spotify.services;

import com.neovisionaries.i18n.CountryCode;
import lombok.SneakyThrows;
import org.apache.hc.core5.http.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.exceptions.SpotifyWebApiException;
import se.michaelthelin.spotify.model_objects.specification.*;

import java.io.IOException;
import java.util.concurrent.CompletableFuture;

@Component
public class SpotifyApiService {
    public SpotifyApiConnection spotifyApiConnection;

    private final SpotifyApi spotifyApi = new SpotifyApi.Builder().build();

    @Autowired
    public SpotifyApiService(SpotifyApiConnection spotifyApiConnection) {
        this.spotifyApiConnection = spotifyApiConnection;
    }

    @SneakyThrows
    public Paging<PlaylistTrack> getPlaylist(String playlist) {
        String playlistId = playlist.replaceAll(".*/(\\w+).*", "$1");
        String accessToken = spotifyApiConnection.getAccessToken();

        spotifyApi.setAccessToken(accessToken);
        spotifyApi.getPlaylist(playlistId).build().execute();
        return spotifyApi.getPlaylistsItems(playlistId).fields("items(track)").build().execute();
    }

    @SneakyThrows
    public Playlist getPlaylistInfo(String playlist) {
        String playlistId = playlist.replaceAll(".*/(\\w+).*", "$1");
        String accessToken = spotifyApiConnection.getAccessToken();

        spotifyApi.setAccessToken(accessToken);
        return spotifyApi.getPlaylist(playlistId).build().execute();
    }

    @SneakyThrows
    public Track getTrack(String track) {
        String trackId = track.replaceAll(".*/(\\w+).*", "$1");
        String accessToken = spotifyApiConnection.getAccessToken();

        spotifyApi.setAccessToken(accessToken);
        return spotifyApi.getTrack(trackId).build().execute();
    }

    @SneakyThrows
    public Paging<TrackSimplified> getAlbum(String album) {
        String albumId = album.replaceAll(".*/(\\w+).*", "$1");
        String accessToken = spotifyApiConnection.getAccessToken();

        spotifyApi.setAccessToken(accessToken);
        return spotifyApi.getAlbumsTracks(albumId).build().execute();
    }
    @SneakyThrows
    public Album getAlbumInfo(String album) {
        String albumId = album.replaceAll(".*/(\\w+).*", "$1");
        String accessToken = spotifyApiConnection.getAccessToken();

        spotifyApi.setAccessToken(accessToken);
        return spotifyApi.getAlbum(albumId).build().execute();
    }


    @SneakyThrows
    public Track[] getArtist(String artist) {
        String artistId = artist.replaceAll(".*/(\\w+).*", "$1");
        String accessToken = spotifyApiConnection.getAccessToken();

        spotifyApi.setAccessToken(accessToken);
        return spotifyApi.getArtistsTopTracks(artistId, CountryCode.US).build().execute();
    }

    @Async
    @SneakyThrows
    public CompletableFuture<Paging<PlaylistTrack>> getPlaylistItemsAsync(String playlistUrl) {
        String playlistId = getPlaylistId(playlistUrl);
        String accessToken = spotifyApiConnection.getAccessToken();
        spotifyApi.setAccessToken(accessToken);

        try {
            return CompletableFuture.completedFuture(spotifyApi.getPlaylistsItems(playlistId).fields("items(track)").build().execute());
        } catch (IOException | SpotifyWebApiException e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }
    }

    @Async
    public CompletableFuture<Playlist> getPlaylistInfoAsync(String playlistUrl) {
        String playlistId = getPlaylistId(playlistUrl);
        String accessToken = spotifyApiConnection.getAccessToken();
        spotifyApi.setAccessToken(accessToken);

        try {
            return CompletableFuture.completedFuture(spotifyApi.getPlaylist(playlistId).build().execute());
        } catch (IOException | SpotifyWebApiException | ParseException e) {
            e.printStackTrace();
            return CompletableFuture.completedFuture(null);
        }
    }

    private String getPlaylistId(String playlistUrl) {
        return playlistUrl.replaceAll(".*/(\\w+).*", "$1");
    }
}
