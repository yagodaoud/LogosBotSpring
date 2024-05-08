package yagodaoud.com.logos.music.audio.conversion.spotify;

import com.sedmelluq.discord.lavaplayer.player.AudioPlayerManager;
import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.MessageEmbed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import se.michaelthelin.spotify.model_objects.specification.*;
import yagodaoud.com.logos.music.audio.CustomAudioLoadResultHandler;
import yagodaoud.com.logos.music.audio.GuildMusicManager;
import yagodaoud.com.logos.music.audio.conversion.mirror.ExtendedAudioPlaylist;
import yagodaoud.com.logos.music.audio.conversion.spotify.services.SpotifyApiService;

import java.util.ArrayList;
import java.util.Arrays;
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

    @SneakyThrows
    public SpotifyAudioObject getSpotifyObject(String url) {
        if (url.contains("playlist")) {
            return getPlaylistDataAsync(url).get();
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
        return new SpotifyTrack(track.getName() + "`", spotifyTracks, ExtendedAudioPlaylist.Type.PLAYLIST, trackUrl, null, null, 0);
    }

    @Async
    public CompletableFuture<SpotifyAudioObject> getPlaylistDataAsync(String playlistUrl) {
        CompletableFuture<Paging<PlaylistTrack>> playlistItemsFuture = spotifyApiService.getPlaylistItemsAsync(playlistUrl);
        CompletableFuture<Playlist> playlistInfoFuture = spotifyApiService.getPlaylistInfoAsync(playlistUrl);

        return playlistItemsFuture.thenCombine(playlistInfoFuture, (playlistItems, playlistInfo) -> {
            List<AudioTrack> spotifyTracks = new ArrayList<>();
            for (PlaylistTrack playlistTrack : playlistItems.getItems()) {
                Track track = (Track) playlistTrack.getTrack();
                String artistName = track.getArtists()[0].getName();
                String trackName = track.getName();
                spotifyTracks.add(new SpotifyAudioTrack(new AudioTrackInfo(trackName, artistName, track.getDurationMs().longValue(), trackName, false, track.getUri())));
            }

            return new SpotifyPlaylist(playlistInfo.getName() + "` by `" + playlistInfo.getOwner().getDisplayName() + "`", spotifyTracks, ExtendedAudioPlaylist.Type.PLAYLIST, playlistUrl, null, null, spotifyTracks.size());
        });
    }

    private SpotifyAudioObject getArtistData(String artistUrl) {
        List<AudioTrack> spotifyTracks = new ArrayList<>();
        Track[] tracks = spotifyApiService.getArtist(artistUrl);

        for (Track artistTrack : tracks) {
            String artistName = artistTrack.getArtists()[0].getName();
            String trackName = artistTrack.getName();
            spotifyTracks.add(new SpotifyAudioTrack(new AudioTrackInfo(trackName, artistName, artistTrack.getDurationMs().longValue(), trackName, false, artistTrack.getUri())));
        }
        return new SpotifyArtist(tracks[0].getArtists()[0].getName() + "`", spotifyTracks, ExtendedAudioPlaylist.Type.PLAYLIST, artistUrl, null, null, spotifyTracks.size());
    }

    private SpotifyAudioObject getAlbumData(String albumUrl) {
        List<AudioTrack> spotifyTracks = new ArrayList<>();
        TrackSimplified[] albumTracks = spotifyApiService.getAlbum(albumUrl).getItems();

        for (TrackSimplified albumTrack : albumTracks) {
            String artistName = albumTrack.getArtists()[0].getName();
            String trackName = albumTrack.getName();
            spotifyTracks.add(new SpotifyAudioTrack(new AudioTrackInfo(trackName, artistName, albumTrack.getDurationMs().longValue(), trackName, false, albumTrack.getUri())));
        }
        return new SpotifyAlbum(spotifyApiService.getAlbumInfo(albumUrl).getName() + "`", spotifyTracks, ExtendedAudioPlaylist.Type.PLAYLIST, albumUrl, null, null, spotifyTracks.size());
    }

    public String handle(AudioPlayerManager audioPlayerManager, GuildMusicManager musicManager, SpotifyAudioObject spotifyAudioObject, boolean forcePlay, CompletableFuture<MessageEmbed> futureMessage, AtomicReference<MessageEmbed> messageContainer) {
        CustomAudioLoadResultHandler loadResultHandlerImplementation = new CustomAudioLoadResultHandler(musicManager, spotifyAudioObject, forcePlay, futureMessage, messageContainer);

        Arrays.stream(spotifyAudioObject.getSpotifyTracks())
                .map(track -> "ytsearch: " + track.getInfo().title + " " + track.getInfo().author)
                .forEachOrdered(query -> audioPlayerManager.loadItemOrdered(musicManager, query, loadResultHandlerImplementation));

        return getSpotifyMessage(spotifyAudioObject);
    }

    public String getSpotifyMessage(SpotifyAudioObject spotifyAudioObject) {
        String message = "";
        switch (spotifyAudioObject.getSpotifyType()) {
            case TRACK -> message = "Loading Spotify track: `";
            case PLAYLIST -> message = "Loading Spotify playlist: `";
            case ALBUM -> message = "Loading Spotify album: `";
            case ARTIST -> message = "Loading Spotify tracks from `";
        }
        return message + spotifyAudioObject.getName();
    }
}
