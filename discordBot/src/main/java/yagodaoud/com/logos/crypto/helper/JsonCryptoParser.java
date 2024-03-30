package yagodaoud.com.logos.crypto.helper;

import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

public class JsonCryptoParser {
    public String extractPriceFromJson(String jsonResponse, String cryptoSymbol) {
        JSONObject json = new JSONObject(jsonResponse);

        JSONObject data = json.getJSONObject("data");
        JSONObject cryptoData = data.getJSONObject(cryptoSymbol);
        JSONObject quote = cryptoData.getJSONObject("quote");
        JSONObject usd = quote.getJSONObject("USD");

        BigDecimal price = usd.getBigDecimal("price");
        return formatPrice(price);
    }

    private String formatPrice(BigDecimal price) {
        NumberFormat format = NumberFormat.getCurrencyInstance(Locale.US);
        return format.format(price);
    }
}
