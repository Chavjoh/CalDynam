package ch.hesso.master.caldynam.repository;

import android.content.Context;

import java.util.List;

import ch.hesso.master.caldynam.CalDynamApplication;
import ch.hesso.master.caldynam.database.Workout;
import ch.hesso.master.caldynam.database.WorkoutDao;

public class WorkoutRepository {

    public static void insertOrUpdate(Context context, Workout workout) {
        getDAO(context).insertOrReplace(workout);
    }

    public static void clear(Context context) {
        getDAO(context).deleteAll();
    }

    public static void delete(Context context, long id) {
        getDAO(context).delete(get(context, id));
    }

    public static List<Workout> getAll(Context context) {
        return getDAO(context).loadAll();
    }

    public static Workout get(Context context, long id) {
        return getDAO(context).load(id);
    }

    private static WorkoutDao getDAO(Context c) {
        return ((CalDynamApplication) c.getApplicationContext()).getDaoSession().getWorkoutDao();
    }

}
