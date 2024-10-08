package yagodaoud.com.logos.music.audio.conversion.spotify;

import com.sedmelluq.discord.lavaplayer.track.AudioTrackInfo;
import com.sedmelluq.discord.lavaplayer.track.playback.LocalAudioTrackExecutor;
import yagodaoud.com.logos.music.audio.conversion.mirror.ExtendedAudioTrack;

public class SpotifyAudioTrack extends ExtendedAudioTrack {


    public SpotifyAudioTrack(AudioTrackInfo trackInfo) {
        this(trackInfo, null, null, null, null, null, false);
    }

    public SpotifyAudioTrack(AudioTrackInfo trackInfo, String albumName, String albumUrl, String artistUrl, String artistArtworkUrl, String previewUrl, boolean isPreview) {
        super(trackInfo, albumName, albumUrl, artistUrl, artistArtworkUrl, previewUrl, isPreview);
    }

    @Override
    public void process(LocalAudioTrackExecutor localAudioTrackExecutor) {

    }
}
