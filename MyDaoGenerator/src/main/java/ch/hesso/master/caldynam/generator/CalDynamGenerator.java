package ch.hesso.master.caldynam.generator;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Schema;

public class CalDynamGenerator {

    public static void main(String args[]) throws Exception {
        Schema schema = new Schema(1, "ch.hesso.master.caldynam.database");

        /*
        Entity box = schema.addEntity("Box");
        box.addIdProperty();
        box.addStringProperty("name");
        box.addIntProperty("slots");
        box.addStringProperty("description");
        */

        new DaoGenerator().generateAll(schema, args[0]);
    }

}
