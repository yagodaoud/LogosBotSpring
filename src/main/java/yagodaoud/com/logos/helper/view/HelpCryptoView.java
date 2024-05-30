package yagodaoud.com.logos.helper.view;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateBuilder;
import net.dv8tion.jda.api.utils.messages.MessageCreateData;
import yagodaoud.com.logos.tools.Colors;

import java.util.Arrays;
import java.util.List;

public class HelpCryptoView {
    private static final List<String> methodNames = Arrays.asList(
            "crypto-price",
            "bitcoin-percentage-alert",
            "bitcoin-price-tracker",
            "bitcoin-price-scheduler"
    );

    public static MessageCreateData getCryptoView(){
        StringBuilder descriptionBuilder = new StringBuilder();

        for (int i = 0; i < methodNames.size(); i++) {
            descriptionBuilder.append("`").append(methodNames.get(i)).append("`");
            if (i < methodNames.size() - 1) {
                descriptionBuilder.append(", ");
            }
        }

        EmbedBuilder builder = new EmbedBuilder();
        builder.setTitle("\uD83D\uDCB8 Crypto Commands")
                .setColor(Colors.CRYPTO_HELP_VIEW)
                .setThumbnail("https://cdn-icons-png.flaticon.com/512/749/749024.png?w=826&t=st=1689123378~exp=1689123978~hmac=e975ec34409fd04e619d0f301ab29850bfdebdc13c749d2ee39b9f00bd8dcf9c")
                .setDescription(descriptionBuilder.toString());

        MessageCreateBuilder messageBuilder = new MessageCreateBuilder()
                .addEmbeds(builder.build());

        return messageBuilder.build();
    }
}
