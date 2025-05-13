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
        "/api/users/add"
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
            // مسیرهای عمومی را بدون احراز هویت اجازه می‌دهیم
            if (isPublicPath(path)) {
                filterChain.doFilter(request, response);
                return;
            }
            
            // بررسی خاص برای مسیر خروج کاربر
            if (path.equals("/api/users/logout")) {
                String token = extractToken(request);
                if (token != null) {
                    String username = sessionService.validateToken(token);
                    if (username != null) {
                        // بارگذاری کاربر و ذخیره در AuthenticationUtils
                        User user = userService.findUser(username);
                        if (user != null) {
                            AuthenticationUtils.login(user);
                        }
                        
                        // اجازه ادامه پردازش درخواست
                        filterChain.doFilter(request, response);
                        return;
                    }
                }
                
                // اگر توکن معتبر نبود، خطای عدم دسترسی برمی‌گردانیم
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"status\":\"error\",\"message\":\"Invalid token for logout\"}");
                return;
            }
            
            // استخراج توکن از درخواست
            String token = extractToken(request);
            if (token == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"status\":\"error\",\"message\":\"Authentication required\"}");
                return;
            }
            
            // اعتبارسنجی توکن با استفاده از SessionService
            String username = sessionService.validateToken(token);
            if (username == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"status\":\"error\",\"message\":\"Invalid or expired session\"}");
                return;
            }
            
            // بارگذاری کاربر با استفاده از نام کاربری دریافت شده
            User user = userService.findUser(username);
            if (user == null) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"status\":\"error\",\"message\":\"User not found\"}");
                return;
            }
            
            // ذخیره اطلاعات کاربر در ThreadLocal با استفاده از AuthenticationUtils
            AuthenticationUtils.login(user);
            
            // ادامه پردازش درخواست
            filterChain.doFilter(request, response);
        } finally {
            // پاکسازی ThreadLocal پس از اتمام درخواست
            // این برای جلوگیری از نشت حافظه مهم است
            AuthenticationUtils.logout();
        }
    }
    
    /**
     * استخراج توکن از هدر Authorization درخواست
     * 
     * @param request درخواست HTTP
     * @return توکن استخراج شده یا null اگر وجود نداشت
     */
    private String extractToken(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }
        return null;
    }
    
    /**
     * بررسی اینکه آیا مسیر از مسیرهای عمومی است
     * 
     * @param path مسیر درخواست
     * @return true اگر مسیر عمومی باشد
     */
    private boolean isPublicPath(String path) {
        return PUBLIC_PATHS.stream().anyMatch(path::equals);
    }
}