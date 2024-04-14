package yagodaoud.com.logos.music.audio.conversion.spotify;

import com.sedmelluq.discord.lavaplayer.track.AudioTrack;

public interface SpotifyAudioObject {
    AudioTrack[] getSpotifyTracks();
    SpotifyObjectType getSpotifyType();
}
