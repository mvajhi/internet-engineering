package org.example.utils;

import org.example.entities.Role;
import org.example.entities.User;
import org.springframework.stereotype.Component;

import java.util.Objects;

/**
 * ابزار کمکی برای مدیریت احراز هویت کاربران در هر درخواست
 * این کلاس از ThreadLocal برای نگهداری اطلاعات کاربر جاری در هر thread استفاده می‌کند
 */
@Component
public class AuthenticationUtils {
    /**
     * ThreadLocal برای نگهداری اطلاعات کاربر جاری در thread فعلی
     * این امکان استفاده همزمان چندین کاربر را فراهم می‌کند
     */
    private static final ThreadLocal<User> currentUser = new ThreadLocal<>();

    /**
     * ثبت کاربر به عنوان کاربر لاگین شده در thread جاری
     * @param user اطلاعات کاربر
     */
    public static void login(User user) {
        if (user != null) {
            currentUser.set(user.copy());
        }
    }

    /**
     * خروج کاربر از سیستم در thread جاری
     */
    public static void logout() {
        currentUser.remove();
    }

    /**
     * بررسی وضعیت ورود کاربر در thread جاری
     * @return آیا کاربری در thread جاری وارد سیستم شده است
     */
    public static boolean loggedIn() {
        return currentUser.get() != null;
    }

    /**
     * بررسی اینکه آیا کاربر مشخص شده همان کاربر وارد شده در thread جاری است
     * @param user کاربری که باید بررسی شود
     * @return نتیجه مقایسه
     */
    public static boolean loggedIn(User user) {
        User current = currentUser.get();
        if (user != null && current != null && Objects.equals(user.getUsername(), current.getUsername())) {
            return true;
        }
        return false;
    }

    /**
     * دریافت نام کاربری کاربر جاری در thread فعلی
     * @return نام کاربری یا null اگر کاربری وارد سیستم نشده باشد
     */
    public static String getUsername() {
        User user = currentUser.get();
        if (user == null)
            return null;
        return user.getUsername();
    }

    /**
     * بررسی دسترسی کاربر جاری به منابع کاربر با نام کاربری مشخص شده
     * @param username نام کاربری که باید دسترسی به آن بررسی شود
     * @return true اگر کاربر جاری همان کاربر مشخص شده یا ادمین باشد
     */
    public static boolean hasAccess(String username) {
        User user = currentUser.get();
        if (user == null) return false;
        if (Objects.equals(username, user.getUsername()) || user.getRole() == Role.ADMIN)
            return true;
        return false;
    }

    /**
     * دریافت اطلاعات کامل کاربر جاری در thread فعلی
     * @return شی کاربر یا null اگر کاربری وارد سیستم نشده باشد
     */
    public static User getLoggedInUser() {
        return currentUser.get();
    }
}
