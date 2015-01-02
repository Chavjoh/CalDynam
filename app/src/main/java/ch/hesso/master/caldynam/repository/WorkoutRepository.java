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
            // Calories is for 60min and 100kg person
            // Date from http://www.tabledescalories.com/calories-brulees.html
            add(new Workout(null, context.getString(R.string.walking), 714, R.drawable.ic_walking_48dp));
            add(new Workout(null, context.getString(R.string.running), 1322, R.drawable.ic_running_48dp));
            add(new Workout(null, context.getString(R.string.cycling), 595, R.drawable.ic_cycling_48dp));
            add(new Workout(null, context.getString(R.string.tennis), 661, R.drawable.ic_tennis_48dp));
            add(new Workout(null, context.getString(R.string.swimming), 939, R.drawable.ic_swimming_48dp));
            add(new Workout(null, context.getString(R.string.other), 1, R.drawable.ic_other_48dp));
        }};

        for (Workout workout : listWorkout) {
            insertOrUpdate(context, workout);
        }
    }

}
