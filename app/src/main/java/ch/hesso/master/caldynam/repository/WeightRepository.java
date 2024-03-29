package ch.hesso.master.caldynam.repository;

import android.content.Context;

import java.util.Date;
import java.util.List;

import ch.hesso.master.caldynam.CalDynamApplication;
import ch.hesso.master.caldynam.database.Weight;
import ch.hesso.master.caldynam.database.WeightDao;
import ch.hesso.master.caldynam.util.DateUtils;

public class WeightRepository {

    public static void insertOrUpdate(Context context, Weight weight) {
        getDAO(context).insertOrReplace(weight);
    }

    public static void clear(Context context) {
        getDAO(context).deleteAll();
    }

    public static void delete(Context context, long id) {
        getDAO(context).delete(get(context, id));
    }

    public static void deleteAll(Context context) {
        getDAO(context).deleteAll();
    }

    public static List<Weight> getAllLimit(Context context, int limit) {
        return getDAO(context).queryBuilder()
                .limit(limit)
                .orderDesc(WeightDao.Properties.Date)
                .list();
    }

    public static boolean hasToday(Context context) {
        List<Weight> listResult = getAllLimit(context, 1);
        return (listResult.size() == 0) ?
                false :
                (DateUtils.sameDay(listResult.get(0).getDate(), new Date()));
    }

    public static Weight getLast(Context context) {
        List<Weight> listResult = getAllLimit(context, 1);
        return listResult.size() == 0 ? null : listResult.get(0);
    }

    public static List<Weight> getPeriod(Context context, Date start, Date end) {
        return getDAO(context).queryBuilder()
                .where(WeightDao.Properties.Date.between(start, end))
                .orderAsc(WeightDao.Properties.Date)
                .list();
    }

    public static List<Weight> getAll(Context context) {
        return getDAO(context).loadAll();
    }

    public static Weight get(Context context, long id) {
        return getDAO(context).load(id);
    }

    private static WeightDao getDAO(Context c) {
        return ((CalDynamApplication) c.getApplicationContext()).getDaoSession().getWeightDao();
    }

}
