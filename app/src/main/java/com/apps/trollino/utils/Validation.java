package com.apps.trollino.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Validation {
    public static boolean isCorrectEmail(String email) {
        Pattern pattern = Pattern.compile("^[a-zA-Z0-9]{1,}" + "((\\.|\\_|-{0,1})[a-zA-Z0-9]{1,})*" + "@" + "[a-zA-Z0-9]{1,}" + "((\\.|\\_|-{0,1})[a-zA-Z0-9]{1,})*" + "\\.[a-zA-Z]{2,}$");
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    public static boolean isCorrectPassword(String password) {
        if (password.length() < 4) {
            return false;
        }
        return true;
    }
}
