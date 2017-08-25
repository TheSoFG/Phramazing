package com.bytelicious.phramazing.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @author ylyubenov
 */

public class DateUtils {

    private static final SimpleDateFormat phramazeFormat = new SimpleDateFormat("dd-MM-yyyy",
            Locale.US);

    public static String formatPhramazeDate(Date date) {
        return phramazeFormat.format(date);
    }

}
