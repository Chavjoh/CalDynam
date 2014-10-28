package ch.hesso.master.caldynam;

import android.app.Application;
import android.database.sqlite.SQLiteDatabase;

import ch.hesso.master.caldynam.database.DaoMaster;
import ch.hesso.master.caldynam.database.DaoSession;

public class CalDynamApplication extends Application {

    public DaoSession daoSession;

    @Override
    public void onCreate() {
        super.onCreate();
        setupDatabase();
    }

    private void setupDatabase() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "caldynam-db", null);
        SQLiteDatabase db = helper.getWritableDatabase();
        DaoMaster daoMaster = new DaoMaster(db);
        daoSession = daoMaster.newSession();
    }

    public DaoSession getDaoSession() {
        return daoSession;
    }

}
