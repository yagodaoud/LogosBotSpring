package yagodaoud.com.logos.music.spotify.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import yagodaoud.com.logos.music.spotify.services.SpotifyApiService;

import java.util.TreeMap;

@Component
public class SpotifyHandler {

    private final SpotifyApiService spotifyApiService;

    @Autowired
    public SpotifyHandler(SpotifyApiService spotifyApiService) {
        this.spotifyApiService = spotifyApiService;
    }

    public TreeMap<String, String> getPlaylistData(String playlistUrl) {
        TreeMap<String, String> trackNames = new TreeMap<>();

        for(PlaylistTrack track : this.spotifyApiService.getPlaylist(playlistUrl).getTracks().getItems()) {
            trackNames.put(this.spotifyApiService.getTrackArtist(track.getTrack().getId()), track.getTrack().getName());
        }
        return trackNames;
    }

    //    https://open.spotify.com/playlist/61m70lFXWxdxPRMr9GTJHi?si=b6445cac01374dfd
}
