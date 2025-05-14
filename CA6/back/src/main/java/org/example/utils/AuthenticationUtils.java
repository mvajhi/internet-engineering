package org.example.utils;

import org.example.entities.Role;
import org.example.entities.User;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AuthenticationUtils {
    private static final ThreadLocal<User> currentUser = new ThreadLocal<>();

    public static void login(User user) {
        if (user != null) {
            currentUser.set(user.copy());
        }
    }

    public static void logout() {
        currentUser.remove();
    }

    public static boolean loggedIn() {
        return currentUser.get() != null;
    }

    public static boolean loggedIn(User user) {
        User current = currentUser.get();
        if (user != null && current != null && Objects.equals(user.getUsername(), current.getUsername())) {
            return true;
        }
        return false;
    }

    public static String getUsername() {
        User user = currentUser.get();
        if (user == null)
            return null;
        return user.getUsername();
    }

    public static boolean hasAccess(String username) {
        User user = currentUser.get();
        if (user == null) return false;
        if (Objects.equals(username, user.getUsername()) || user.getRole() == Role.ADMIN)
            return true;
        return false;
    }

    public static User getLoggedInUser() {
        return currentUser.get();
    }
}
