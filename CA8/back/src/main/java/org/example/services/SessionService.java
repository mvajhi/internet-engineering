package org.example.services;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SessionService {

    private static final long SESSION_TIMEOUT_MINUTES = 20;
    private static final String TOKEN_PREFIX = "session:token:";
    private static final String USER_PREFIX = "session:user:";
    private static final String SECRET_KEY = "AliAndMahdiSecretKeYHaHaHaHaHaHaIFYouCanHackThisSiteYouAreVerySmart";
    private static final long EXPIRATION_TIME = 86400000; // 1 day in milliseconds

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;


    public String createSession(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuer("MoBook")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    public String validateToken(String token) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();

            if (claims.getIssuer() == null || !claims.getIssuer().equals("MoBook")) {
                return null;
            }

            if (claims.getSubject() == null) {
                return null;
            }

            return claims.getSubject();
        } catch (Exception e) {
            return null;
        }
    }

    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}