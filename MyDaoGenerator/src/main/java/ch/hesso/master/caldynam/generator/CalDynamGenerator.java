package ch.hesso.master.caldynam.generator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;

public class CalDynamGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "ch.hesso.master.caldynam.database");

        /**
         * FOOD CATEGORY
         */
        Entity foodCategory = schema.addEntity("FoodCategory");
        foodCategory.addIdProperty().autoincrement();
        foodCategory.addStringProperty("name").notNull().indexAsc(null, false);

        /**
         * FOOD
         */
        Entity food = schema.addEntity("Food");
        food.addIdProperty().autoincrement();
        food.addStringProperty("name").notNull().indexAsc(null, false);
        Property foodCategoryId = food.addLongProperty("categoryId").notNull().index().getProperty();
        food.addToOne(foodCategory, foodCategoryId);
        food.addIntProperty("calorie").notNull();
        food.addStringProperty("image");
        food.addStringProperty("barcode");

        /**
         * WORKOUT
         */
        Entity workout = schema.addEntity("Workout");
        workout.addIdProperty().autoincrement();
        workout.addStringProperty("name").notNull().indexAsc(null, false);
        workout.addIntProperty("calorie").notNull();

        /**
         * WEIGHT
         */
        Entity weight = schema.addEntity("Weight");
        weight.addIdProperty().autoincrement();
        weight.addDateProperty("date").notNull().indexAsc(null, false);
        weight.addFloatProperty("weight").notNull();

        /**
         * LOGGING
         */
        Entity logging = schema.addEntity("Logging");
        logging.addIdProperty().autoincrement();
        logging.addDateProperty("date").notNull().indexAsc(null, false);
        Property foodId = logging.addLongProperty("foodId").index().getProperty();
        logging.addToOne(food, foodId);
        Property workoutId = logging.addLongProperty("workoutId").index().getProperty();
        logging.addToOne(workout, workoutId);


        new DaoGenerator().generateAll(schema, args[0]);
    }

}
