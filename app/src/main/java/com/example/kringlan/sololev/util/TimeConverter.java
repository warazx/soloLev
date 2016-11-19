package com.example.kringlan.sololev.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class TimeConverter {
    public static String toString(long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(time));
    }
}
