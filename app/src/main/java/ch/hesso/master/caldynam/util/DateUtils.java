package ch.hesso.master.caldynam.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import ch.hesso.master.caldynam.Constants;

public class DateUtils {

    public enum DAY_PARTING {
        MORNING,
        DAYTIME,
        EVENING
    }

    public static Date endOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(Constants.TIMEZONE);
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        calendar.set(Calendar.MILLISECOND, 999);
        return calendar.getTime();
    }

    public static Date startOfDay(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(Constants.TIMEZONE);
        calendar.setTime(date);
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static boolean sameDay(Date date1, Date date2) {
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        cal1.setTimeZone(Constants.TIMEZONE);
        cal2.setTimeZone(Constants.TIMEZONE);
        cal1.setTime(date1);
        cal2.setTime(date2);
        return cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR) &&
                cal1.get(Calendar.DAY_OF_YEAR) == cal2.get(Calendar.DAY_OF_YEAR);
    }

    public static Date todayDayParting(DAY_PARTING dayParting) {
        int hourOfDay = 6;
        if (dayParting == DAY_PARTING.DAYTIME) {
            hourOfDay = 12;
        } else if (dayParting == DAY_PARTING.EVENING) {
            hourOfDay = 18;
        }
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(Constants.TIMEZONE);
        calendar.setTime(new Date());
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        return calendar.getTime();
    }

    public static DAY_PARTING dayParting(Date date) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(Constants.TIMEZONE);
        calendar.setTime(date);
        if (calendar.get(Calendar.HOUR_OF_DAY) > 4 && calendar.get(Calendar.HOUR_OF_DAY) < 12) {
            return DAY_PARTING.MORNING;
        } else if (calendar.get(Calendar.HOUR_OF_DAY) < 18) {
            return DAY_PARTING.DAYTIME;
        }
        return DAY_PARTING.EVENING;
    }

    public static String dateToString(Date date) {
        return dateToString(Constants.DATE_FORMAT, date);
    }

    public static String dateToString(String pattern, Date date) {
        SimpleDateFormat dateFormat = new SimpleDateFormat();
        dateFormat.setTimeZone(Constants.TIMEZONE);
        dateFormat.applyPattern(pattern);
        return dateFormat.format(date);
    }

    public static String dateHourToString(Date date) {
        return dateToString(Constants.DATE_HOUR_FORMAT, date);
    }
}
