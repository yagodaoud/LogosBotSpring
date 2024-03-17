package yagodaoud.com.logos.crypto.services;

import io.github.cdimascio.dotenv.Dotenv;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import yagodaoud.com.logos.crypto.JsonCryptoParser;

@Service
public class CoinMarketCapApiService {
    private static final String apiKey = Dotenv.configure().load().get("TOKENCMC");

    private final RestTemplate restTemplate;

    @Autowired
    public CoinMarketCapApiService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public String getCryptoPrice(String cryptoSymbol) {
        String apiUrl = "https://pro-api.coinmarketcap.com/v1/cryptocurrency/quotes/latest?symbol={crypto}";
        String url = apiUrl.replace("{crypto}", cryptoSymbol);

        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CMC_Pro_API_Key", apiKey);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        return new JsonCryptoParser().extractPriceFromJson(response.getBody(), cryptoSymbol);
    }
}
