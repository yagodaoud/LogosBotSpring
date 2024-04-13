package yagodaoud.com.logos.music.audio.conversion.spotify;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import yagodaoud.com.logos.music.audio.conversion.mirror.ExtendedAudioPlaylist;

import java.util.List;

public class SpotifyAudioPlaylist extends ExtendedAudioPlaylist {

    public SpotifyAudioPlaylist(String name, List<AudioTrack> tracks, ExtendedAudioPlaylist.Type type, String url, String artworkURL, String author, Integer totalTracks) {
        super(name, tracks, type, url, artworkURL, author, totalTracks);
    }

}
