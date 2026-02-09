package com.example.bankcards.util;

import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.io.Encoders;
import lombok.experimental.UtilityClass;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

@UtilityClass
public class CardNumberHasher {
    private static final String ALGORITHM = "HmacSHA256";

    public static String hash(String number, String base64Secret) throws IllegalStateException {
        try {
            Mac mac = Mac.getInstance(ALGORITHM);

            byte[] keyBytes = Decoders.BASE64.decode(base64Secret);
            SecretKeySpec secretKeySpec = new SecretKeySpec(keyBytes, ALGORITHM);

            mac.init(secretKeySpec);

            byte[] rawHmac = mac.doFinal(number.getBytes(StandardCharsets.UTF_8));

            return Encoders.BASE64.encode(rawHmac);

        } catch (NoSuchAlgorithmException | InvalidKeyException e) {
            throw new IllegalStateException("Хеширование карты не удалось");
        }
    }
}
