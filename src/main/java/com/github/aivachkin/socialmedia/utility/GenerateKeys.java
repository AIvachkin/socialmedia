package com.github.aivachkin.socialmedia.utility;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Encoders;
import io.jsonwebtoken.security.Keys;

// Класс для получения ключей с выводом результата в консоль
public class GenerateKeys {

    public static void main(String[] args) {
        System.out.println(generateKey());
        System.out.println(generateKey());
    }

    private static String generateKey() {
        return Encoders.BASE64.encode(Keys.secretKeyFor(SignatureAlgorithm.HS512).getEncoded());
    }

}
