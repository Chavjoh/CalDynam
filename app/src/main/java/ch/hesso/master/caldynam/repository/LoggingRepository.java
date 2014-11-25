package ch.hesso.master.caldynam.repository;

import android.content.Context;

import java.util.Date;
import java.util.List;

import ch.hesso.master.caldynam.CalDynamApplication;
import ch.hesso.master.caldynam.database.Logging;
import ch.hesso.master.caldynam.database.LoggingDao;
import ch.hesso.master.caldynam.util.DateUtils;

public class LoggingRepository {

    public static void insertOrUpdate(Context context, Logging logging) {
        getDAO(context).insertOrReplace(logging);
    }

    public static void clear(Context context) {
        getDAO(context).deleteAll();
    }

    public static void delete(Context context, long id) {
        getDAO(context).delete(get(context, id));
    }

    public static List<Logging> getToday(Context context, Date today) {
        Date startOfDay = DateUtils.startOfDay(today);
        Date endOfDay = DateUtils.endOfDay(today);
        return getDAO(context).queryBuilder()
                .where(LoggingDao.Properties.Date.between(startOfDay, endOfDay))
                .orderAsc(LoggingDao.Properties.Date)
                .list();
    }

    public static List<Logging> getAll(Context context) {
        return getDAO(context).loadAll();
    }

    public static Logging get(Context context, long id) {
        return getDAO(context).load(id);
    }

    private static LoggingDao getDAO(Context c) {
        return ((CalDynamApplication) c.getApplicationContext()).getDaoSession().getLoggingDao();
    }

}
