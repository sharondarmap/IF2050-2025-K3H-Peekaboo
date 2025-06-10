package com.pekaboo.util;

import com.pekaboo.entities.User;

public class Session { //untuk ngambil session user
    private static User currentUser;

    public static void setCurrentUser(User user) {
        currentUser = user;
    }

    public static User getCurrentUser() {
        return currentUser;
    }

    public static void clear() {
        currentUser = null;
    }
}
