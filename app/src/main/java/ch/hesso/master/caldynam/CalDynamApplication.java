package ch.hesso.master.caldynam;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;

import ch.hesso.master.caldynam.database.DaoMaster;
import ch.hesso.master.caldynam.database.DaoSession;
import ch.hesso.master.caldynam.repository.FoodCategoryRepository;

public class CalDynamApplication extends Application {

    private static final String KEY_DATABASE_POPULATE = "DATABASE_POPULATE";

    private static Context context;

    public DaoSession daoSession;
    private SharedPreferences settings;

    @Override
    public void onCreate() {
        super.onCreate();

        CalDynamApplication.context = getApplicationContext();
        this.settings = context.getSharedPreferences(Constants.PROJECT_NAME, 0);

        this.setupDatabase();
        this.populateDatabase();
    }

    private void setupDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "caldynam-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    private void populateDatabase() {
        boolean hasDatabasePopulation = settings.getBoolean(KEY_DATABASE_POPULATE, false);

        if (!hasDatabasePopulation) {
            FoodCategoryRepository.populate(context);

            SharedPreferences.Editor editor = settings.edit();
            editor.putBoolean(KEY_DATABASE_POPULATE, true);
            editor.commit();
        }
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

    /**
     * @return the app context
     */
    public static Context getAppContext()
    {
        return CalDynamApplication.context;
    }

}
