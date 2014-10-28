package ch.hesso.master.caldynam.util;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class DateUtils {

    public static final Locale LOCALE = new Locale("fr", "CH");
    public static final TimeZone TIMEZONE = TimeZone.getTimeZone("Europe/Zurich");

    public static Date endOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TIMEZONE);
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Date startOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TIMEZONE);
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

}
