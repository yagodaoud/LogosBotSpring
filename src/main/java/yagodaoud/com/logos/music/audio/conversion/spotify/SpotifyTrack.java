package yagodaoud.com.logos.music.audio.conversion.spotify;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;
import yagodaoud.com.logos.music.audio.conversion.mirror.ExtendedAudioPlaylist;

import java.util.List;

public class SpotifyTrack extends ExtendedAudioPlaylist implements SpotifyAudioObject {
    public SpotifyTrack(String name, List<AudioTrack> tracks, ExtendedAudioPlaylist.Type type, String url, String artworkURL, String author, Integer totalTracks) {
        super(name, tracks, type, url, artworkURL, author, totalTracks);
    }

    @Override
    public AudioTrack[] getSpotifyTracks() {
        return getTracks().toArray(new AudioTrack[0]);
    }

    @Override
    public SpotifyObjectType getSpotifyType() {
        return SpotifyObjectType.TRACK;
    }
}
