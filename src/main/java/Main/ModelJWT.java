package Main;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class ModelJWT {
    private final static byte[] API_KEY_BYTES = "qwertyuioplkjhgfdsaazxcvbnmqwertyuiokjhgfdsazxcvbnmuytresxcv".getBytes(StandardCharsets.UTF_8);
    private static SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    private static Key signingKey = new SecretKeySpec(API_KEY_BYTES, signatureAlgorithm.getJcaName());
    private final static Key KEY = Keys.secretKeyFor(signatureAlgorithm);

    public static String createJWT(String s) {
        Date now = new Date();

        return Jwts.builder()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + TimeUnit.HOURS.toMillis(12)))
                .setSubject(s)
                .signWith(signingKey, signatureAlgorithm)
                .compact();
    }

    public static String getUserLoginFromJWT(String s) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(signingKey)
                .build()
                .parseClaimsJws(s)
                .getBody();
        System.out.println("User: " + claims.getSubject());
        System.out.println("Expiration: " + claims.getExpiration());
        return claims.getSubject();
    }

    public static String generateJwt(String s) {
        return Jwts.builder()
                .setSubject(s)
                .signWith(KEY)
                .compact();
    }

    public static String getLoginFromJWT(String s) {
        return Jwts.parserBuilder()
                .setSigningKey(KEY)
                .build()
                .parseClaimsJws(s)
                .getBody()
                .getSubject();
    }

}
