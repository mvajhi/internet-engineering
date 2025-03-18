package org.example.utils;

import org.example.entities.User;
import org.springframework.stereotype.Component;

@Component
public class AuthenticationUtils {
    public static User loggedInUser;

    public static void login(User user) {
        loggedInUser = user;
    }

    public static boolean loggedIn() {
        return loggedInUser != null;
    }
}
