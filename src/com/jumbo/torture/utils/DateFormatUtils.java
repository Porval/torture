package com.jumbo.torture.utils;

import android.content.Context;
import android.text.format.DateUtils;
import android.text.format.Time;


public class DateFormatUtils{
    public static String formatTimeStampString(Context context, long when) {
        return formatTimeStampString(context, when, false);
    }

    public static String formatTimeStampString(Context context, long when,
            boolean fullFormat) {
        Time then = new Time();
        then.set(when);
        Time now = new Time();
        now.setToNow();

        int format_flags = DateUtils.FORMAT_NO_NOON_MIDNIGHT
                | DateUtils.FORMAT_ABBREV_ALL | DateUtils.FORMAT_CAP_AMPM;

        if (then.year != now.year) {
            format_flags |= DateUtils.FORMAT_SHOW_YEAR
                    | DateUtils.FORMAT_SHOW_DATE;
        } else if (then.yearDay != now.yearDay) {
            format_flags |= DateUtils.FORMAT_SHOW_DATE;
        } else {
            format_flags |= DateUtils.FORMAT_SHOW_TIME;
        }

        if (fullFormat) {
            format_flags |= (DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME);
        }

        return DateUtils.formatDateTime(context, when, format_flags);
    }

    public static String formatTimeStampStringForMessage(Context context,
            long when) {
        Time then = new Time();
        then.set(when);
        Time now = new Time();
        now.setToNow();

        int format_flags = DateUtils.FORMAT_NO_NOON_MIDNIGHT
                | DateUtils.FORMAT_ABBREV_ALL | DateUtils.FORMAT_CAP_AMPM;
        if (then.year == now.year && then.yearDay == now.yearDay) {
            format_flags |= DateUtils.FORMAT_SHOW_TIME;
        } else {
            if (then.year != now.year) {
                format_flags |= DateUtils.FORMAT_SHOW_YEAR;
            }
            format_flags |= (DateUtils.FORMAT_SHOW_DATE | DateUtils.FORMAT_SHOW_TIME);
        }

        return DateUtils.formatDateTime(context, when, format_flags);
    }

}