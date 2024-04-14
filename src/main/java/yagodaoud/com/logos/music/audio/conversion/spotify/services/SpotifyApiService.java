package yagodaoud.com.logos.music.audio.conversion.spotify.services;

import com.neovisionaries.i18n.CountryCode;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.SpotifyApi;
import se.michaelthelin.spotify.model_objects.specification.Paging;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import se.michaelthelin.spotify.model_objects.specification.Track;
import se.michaelthelin.spotify.model_objects.specification.TrackSimplified;

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
        return spotifyApi.getPlaylistsItems(playlistId).fields("items(track)").build().execute();
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
        String playlistId = album.replaceAll(".*/(\\w+).*", "$1");
        String accessToken = spotifyApiConnection.getAccessToken();

        spotifyApi.setAccessToken(accessToken);
        return spotifyApi.getAlbumsTracks(playlistId).build().execute();
    }

    @SneakyThrows
    public Track[] getArtist(String artist) {
        String artistId = artist.replaceAll(".*/(\\w+).*", "$1");
        String accessToken = spotifyApiConnection.getAccessToken();

        spotifyApi.setAccessToken(accessToken);
        return spotifyApi.getArtistsTopTracks(artistId, CountryCode.US).build().execute();
    }
}
