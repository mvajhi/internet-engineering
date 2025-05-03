package org.example.utils;

import java.time.LocalDateTime;

public class TimeUtils {

    public static boolean isStillInBorrowInterval(LocalDateTime startData, int borrowedDays) {
        LocalDateTime now = LocalDateTime.now();
        long diff = java.time.Duration.between(startData, now).toDays();
        if(diff <= borrowedDays)
            return true;
        return false;
    }
}
