package com.funix.asm02.common;

import java.text.Normalizer;
import java.util.regex.Pattern;

public class RemoveVietnameseSigns {
    public static String removeVietnameseSignsAndLowerCase(String str) {
        String temp = Normalizer.normalize(str, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        String result = pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replaceAll("đ", "d");
        return result.toLowerCase();
    }
}
