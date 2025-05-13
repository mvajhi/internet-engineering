package org.example.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service
public class SessionService {

    private static final long SESSION_TIMEOUT_MINUTES = 20;
    private static final String TOKEN_PREFIX = "session:token:";
    private static final String USER_PREFIX = "session:user:";

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * Creates a new session for a user and returns the session token
     *
     * @param username the username to create a session for
     * @return the generated session token
     */
    public String createSession(String username) {
        String token = generateToken();
        
        // Store token -> username mapping
        redisTemplate.opsForValue().set(
            TOKEN_PREFIX + token, 
            username, 
            SESSION_TIMEOUT_MINUTES, 
            TimeUnit.MINUTES
        );
        
        // Store username -> token mapping for easy invalidation on logout
        redisTemplate.opsForValue().set(
            USER_PREFIX + username, 
            token, 
            SESSION_TIMEOUT_MINUTES, 
            TimeUnit.MINUTES
        );
        
        return token;
    }

    /**
     * Validates if a token is valid and refreshes its expiration time
     *
     * @param token the token to validate
     * @return the username associated with the token, or null if invalid
     */
    public String validateToken(String token) {
        if (token == null) {
            return null;
        }
        
        String key = TOKEN_PREFIX + token;
        String username = (String) redisTemplate.opsForValue().get(key);
        
        if (username != null) {
            // Refresh token expiration time
            redisTemplate.expire(key, SESSION_TIMEOUT_MINUTES, TimeUnit.MINUTES);
            redisTemplate.expire(USER_PREFIX + username, SESSION_TIMEOUT_MINUTES, TimeUnit.MINUTES);
        }
        
        return username;
    }

    /**
     * Invalidates a user's session
     *
     * @param username the username to invalidate the session for
     */
    public void invalidateSession(String username) {
        String token = (String) redisTemplate.opsForValue().get(USER_PREFIX + username);
        
        if (token != null) {
            redisTemplate.delete(TOKEN_PREFIX + token);
        }
        
        redisTemplate.delete(USER_PREFIX + username);
    }
    
    /**
     * Generates a random UUID token
     *
     * @return a random UUID as string
     */
    private String generateToken() {
        return UUID.randomUUID().toString();
    }
}