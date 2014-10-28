package ch.hesso.master.caldynam.repository;

import android.content.Context;

import java.util.List;

import ch.hesso.master.caldynam.CalDynamApplication;
import ch.hesso.master.caldynam.database.Food;
import ch.hesso.master.caldynam.database.FoodCategory;
import ch.hesso.master.caldynam.database.FoodDao;

public class FoodRepository {

    public static void insertOrUpdate(Context context, Food food) {
        getDAO(context).insertOrReplace(food);
    }

    public static void clear(Context context) {
        getDAO(context).deleteAll();
    }

    public static void delete(Context context, long id) {
        getDAO(context).delete(get(context, id));
    }

    public static List<Food> getAll(Context context) {
        return getDAO(context).loadAll();
    }

    public static List<Food> getAll(Context context, FoodCategory foodCategory) {
        return getDAO(context).queryBuilder()
                .where(FoodDao.Properties.CategoryId.eq(foodCategory.getId()))
                .orderAsc(FoodDao.Properties.Name)
                .list();
    }

    public static Food get(Context context, long id) {
        return getDAO(context).load(id);
    }

    private static FoodDao getDAO(Context c) {
        return ((CalDynamApplication) c.getApplicationContext()).getDaoSession().getFoodDao();
    }

}
