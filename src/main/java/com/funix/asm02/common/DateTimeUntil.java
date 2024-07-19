package com.funix.asm02.common;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
public class DateTimeUntil {
    public static String date(){
        LocalDateTime currentDateTime = LocalDateTime.now();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        String dateTimeString = currentDateTime.format(formatter);
        return dateTimeString;
    }
}
