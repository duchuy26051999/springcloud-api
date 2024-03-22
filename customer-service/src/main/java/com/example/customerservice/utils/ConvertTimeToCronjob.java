package com.example.customerservice.utils;

import java.time.LocalTime;

public class ConvertTimeToCronjob {
    public static String getCronjob(LocalTime time, int day) {
        return "0 " + time.getMinute() + " " + time.getHour() + " " + day + " * ?";
    }
}
