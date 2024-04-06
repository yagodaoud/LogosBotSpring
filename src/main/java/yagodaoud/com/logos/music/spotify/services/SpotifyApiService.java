package yagodaoud.com.logos.music.spotify.services;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Playlist;

@Component
public class SpotifyApiService {
    public SpotifyApiConnection spotifyApiConnection;

    private final SpotifyApi spotifyApi = new SpotifyApi.Builder().build();

    @Autowired
    public SpotifyApiService(SpotifyApiConnection spotifyApiConnection) {
        this.spotifyApiConnection = spotifyApiConnection;
    }

    @SneakyThrows
    public String getArtistName(String artistId) {
        String accessToken = this.spotifyApiConnection.getAccessToken();
        this.spotifyApi.setAccessToken(accessToken);

        return this.spotifyApi.getArtist(artistId).build().execute().getName();
    }

    @SneakyThrows
    public Playlist getPlaylist(String track) {
        String accessToken = spotifyApiConnection.getAccessToken();
        this.spotifyApi.setAccessToken(accessToken);

        return this.spotifyApi.getPlaylist("61m70lFXWxdxPRMr9GTJHi").build().execute();
    }

}
