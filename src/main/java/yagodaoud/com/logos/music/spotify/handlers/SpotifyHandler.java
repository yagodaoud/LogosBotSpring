package yagodaoud.com.logos.music.spotify.handlers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.model_objects.specification.PlaylistTrack;
import yagodaoud.com.logos.music.spotify.services.SpotifyApiService;

import java.util.ArrayList;
import java.util.List;

@Component
public class SpotifyHandler {

    private final SpotifyApiService spotifyApiService;

    @Autowired
    public SpotifyHandler(SpotifyApiService spotifyApiService) {
        this.spotifyApiService = spotifyApiService;
    }

    public List<String> getPlaylistData(String playlistUrl) {
        List<String> trackNames = new ArrayList<>();

        for(PlaylistTrack track : this.spotifyApiService.getPlaylist(playlistUrl).getTracks().getItems()) {
            trackNames.add(track.getTrack().getName());
        }
        return trackNames;
    }

    //    https://open.spotify.com/playlist/61m70lFXWxdxPRMr9GTJHi?si=b6445cac01374dfd
}
