package ch.hesso.master.caldynam.repository;

import android.content.Context;

import java.util.List;

import ch.hesso.master.caldynam.CalDynamApplication;
import ch.hesso.master.caldynam.database.FoodCategory;
import ch.hesso.master.caldynam.database.FoodCategoryDao;

public class FoodCategoryRepository {

    public static void insertOrUpdate(Context context, FoodCategory foodCategory) {
        getDAO(context).insertOrReplace(foodCategory);
    }

    public static void clear(Context context) {
        getDAO(context).deleteAll();
    }

    public static void delete(Context context, long id) {
        getDAO(context).delete(get(context, id));
    }

    public static List<FoodCategory> getAll(Context context) {
        return getDAO(context).loadAll();
    }

    public static FoodCategory get(Context context, long id) {
        return getDAO(context).load(id);
    }

    private static FoodCategoryDao getDAO(Context c) {
        return ((CalDynamApplication) c.getApplicationContext()).getDaoSession().getFoodCategoryDao();
    }

}
