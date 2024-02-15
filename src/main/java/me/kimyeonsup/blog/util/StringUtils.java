package me.kimyeonsup.blog.util;

import java.util.regex.Pattern;

public class StringUtils {

    private static final Pattern SPECIAL_CHARACTER = Pattern.compile("[#*()\\\":{}|]");

    public static String replaceAllSpecialCharacter(String text) {
        return SPECIAL_CHARACTER.matcher(text).replaceAll("");
    }
}
