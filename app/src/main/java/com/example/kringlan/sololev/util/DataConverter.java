package com.example.kringlan.sololev.util;

import java.text.SimpleDateFormat;
import java.util.Date;

public final class DataConverter {
    public static String longToDateString(long time) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm").format(new Date(time));
    }
}
