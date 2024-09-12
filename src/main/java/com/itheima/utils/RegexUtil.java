package com.itheima.utils;


import java.util.regex.Pattern;
import java.util.regex.Matcher;

public class RegexUtil {

    public static <T> void isValidVal(String regex, T val) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(val.toString());
        if (!matcher.matches())throw new RuntimeException("Input does not match the required format " +
                "\"" +
                regex+
                "\""+
                ".");
    }
}
