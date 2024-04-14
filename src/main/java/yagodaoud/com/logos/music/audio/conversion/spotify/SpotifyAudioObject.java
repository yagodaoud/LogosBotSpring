package yagodaoud.com.logos.music.audio.conversion.spotify;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public interface SpotifyAudioObject {
    String getName();
    AudioTrack[] getSpotifyTracks();
    SpotifyObjectType getSpotifyType();
}
