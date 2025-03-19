package org.example.utils;

import org.example.entities.Role;
import org.example.entities.User;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class AuthenticationUtils {
    public static User loggedInUser;

    public static void login(User user) {
        loggedInUser = user.copy();
    }

    public static void logout() {
        loggedInUser = null;
    }

    public static boolean loggedIn() {
        return loggedInUser != null;
    }

    public static boolean loggedIn(User user) {
        if(user != null && user == loggedInUser) {
            return true;
        }
        return false;
    }

    public static String getUsername(){
        return loggedInUser.getUsername();
    }

    public static boolean hasAccess(String username) {
        if (loggedInUser == null) return false;
        if(Objects.equals(username, loggedInUser.getUsername())
                || loggedInUser.getRole() == Role.ADMIN)
            return true;
        return false;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }
}
