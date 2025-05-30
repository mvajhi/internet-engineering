package org.example.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.entities.User;
import org.example.services.SessionService;
import org.example.services.UserService;
import org.example.utils.AuthenticationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
public class AuthenticationFilter extends OncePerRequestFilter {

    private static final List<String> PUBLIC_PATHS = Arrays.asList(
        "/api/users/login",
        "/api/users/add",
        "/api/auth/google"
    );

    @Autowired
    private SessionService sessionService;

    @Autowired
    private UserService userService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        
        try {
            if (isPublicPath(path)) {
                filterChain.doFilter(request, response);
                return;
            }
            
            if (path.equals("/api/users/logout")) {
                String token = extractToken(request);
                if (token != null) {
                    String username = sessionService.validateToken(token);
                    if (username != null) {
                        User user = userService.findUser(username);
                        if (user != null) {
                            AuthenticationUtils.login(user);
                        }
                        
                        filterChain.doFilter(request, response);
                        return;
                    }
                }
                
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"status\":\"error\",\"message\":\"Invalid token for logout\"}");
                return;
            }
            
            String token = extractToken(request);
            if (token == null) {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"status\":\"error\",\"message\":\"Authentication required\"}");
                return;
            }
            
            String username = sessionService.validateToken(token);
            if (username == null) {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"status\":\"error\",\"message\":\"Invalid or expired session\"}");
                return;
            }
            
            User user = userService.findUser(username);
            if (user == null) {
                response.setContentType("application/json");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"status\":\"error\",\"message\":\"User not found\"}");
                return;
            }
            
            AuthenticationUtils.login(user);
            
            filterChain.doFilter(request, response);
        } finally {
            AuthenticationUtils.logout();
        }
    }
    
    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
    
    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::equals);
    }
}