package yagodaoud.com.logos.music.audio.conversion.youtube;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YoutubeScraper {
    private static final Pattern RECOMMENDED_VIDEO_PATTERN = Pattern.compile("\\{\"webCommandMetadata\":\\{\"url\":\"(/watch\\?v=[^\"]+)\"");
    private static final List<String> lastUrls = new ArrayList<>();

    public static String getNextRecommendedVideoUrl(String videoUrl) throws IOException {
        URL url = new URL(videoUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        int responseCode = connection.getResponseCode();

        if (responseCode != HttpURLConnection.HTTP_OK) {
            return null;
        }

        Document doc = Jsoup.parse(connection.getInputStream(), "UTF-8", videoUrl);
        Element body = doc.body();

        Matcher matcher = RECOMMENDED_VIDEO_PATTERN.matcher(body.data());

        List<String> matches = new ArrayList<>();

        int i = 0;
        while (matcher.find() && i < 3) {
            String urlFinal = matcher.group(1).replaceFirst("\\\\u0026.*", "");
            String fullUrl = "https://www.youtube.com" + urlFinal;
            if (!lastUrls.contains(fullUrl)) {
                matches.add(fullUrl);
                i++;
            }
        }

        if (lastUrls.size() >= 6) {
            lastUrls.clear();
        }

        if (!matches.isEmpty()) {
            Random random = new Random();
            int randomIndex = random.nextInt(matches.size());
            String link = matches.get(randomIndex);
            lastUrls.add(link);
            return link;
        }

        return null;
    }
}
