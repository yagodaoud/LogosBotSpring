package yagodaoud.com.logos.music.audio.conversion.spotify.services;

import io.github.cdimascio.dotenv.Dotenv;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class SpotifyApiConnection {
    private static final String CLIENT_ID = Dotenv.configure().load().get("SPOTIFYCLIENTID");
    private static final String CLIENT_SECRET = Dotenv.configure().load().get("SPOTIFYSECRET");
    private static final String TOKEN_URL = "https://accounts.spotify.com/api/token";
    private final RestTemplate restTemplate;

    @Autowired
    public SpotifyApiConnection(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @SneakyThrows
    public String getAccessToken() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setBasicAuth(CLIENT_ID, CLIENT_SECRET);

        String requestBody = "grant_type=client_credentials";

        HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

        ResponseEntity<String> responseEntity = this.restTemplate.exchange(TOKEN_URL, HttpMethod.POST, requestEntity, String.class);

        String accessToken = "";
        if (responseEntity.getStatusCode() == HttpStatus.OK) {

            String responseBody = responseEntity.getBody();
            accessToken = responseBody.split("\"")[3];
        }
        return accessToken;
    }
}
