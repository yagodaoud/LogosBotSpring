package yagodaoud.com.logos.music.spotify.services;

import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;

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

        this.spotifyApi.setAccessToken(accessToken);
        return this.spotifyApi.getPlaylistsItems(playlistId).fields("items(track)").build().execute();
    }

}
