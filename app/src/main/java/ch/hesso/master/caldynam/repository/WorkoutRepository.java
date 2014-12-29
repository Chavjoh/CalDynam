package ch.hesso.master.caldynam.repository;

import android.content.Context;

import java.util.ArrayList;
import java.util.List;

import ch.hesso.master.caldynam.CalDynamApplication;
import ch.hesso.master.caldynam.R;
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

    public static void populate(final Context context) {
        List<Workout> listWorkout = new ArrayList<Workout>() {{
            add(new Workout(null, context.getString(R.string.Walking), 500));
            add(new Workout(null, context.getString(R.string.Running), 900));
            add(new Workout(null, context.getString(R.string.Cycling), 400));
            add(new Workout(null, context.getString(R.string.Other), 1));
        }};

        for (Workout workout : listWorkout) {
            insertOrUpdate(context, workout);
        }
    }

}
