package yagodaoud.com.logos.music.spotify.handlers;

public class SpotifyAudioTrack{

    private final String query;

    public SpotifyAudioTrack(String trackArtist, String trackTitle) {
        this.query = "ytsearch:" + " " + trackArtist + " " + trackTitle;
    }

    public String getQuery() {
        return this.query;
    }

}
