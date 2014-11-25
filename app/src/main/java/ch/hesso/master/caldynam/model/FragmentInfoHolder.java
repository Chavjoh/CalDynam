package ch.hesso.master.caldynam.model;

import ch.hesso.master.caldynam.CalDynamApplication;
import ch.hesso.master.caldynam.R;

/**
 * Created by alexandreperez on 25.11.14.
 */
public class FragmentInfoHolder {

    public final static FragmentInfoHolder[] FRAGMENT_INFO_HOLDERS = {
            new FragmentInfoHolder(CalDynamApplication.getAppContext().getString(R.string.section_summary), false, false),
            new FragmentInfoHolder(CalDynamApplication.getAppContext().getString(R.string.section_weight_measurement), false, false),
            new FragmentInfoHolder(CalDynamApplication.getAppContext().getString(R.string.section_meal_logging), true, true),
            new FragmentInfoHolder(CalDynamApplication.getAppContext().getString(R.string.section_food_catalog), true, false), // TODO: random values
    };

    private String mName;
    private boolean mLargeToolbar;
    private boolean mFABVisible;

   private FragmentInfoHolder(String name, boolean largeToolbar, boolean FABVisible) {
       mName = name;
       mLargeToolbar = largeToolbar;
       mFABVisible = FABVisible;
   }

    public String getName() {
        return mName;
    }

    public boolean isLargeToolbar() {
        return mLargeToolbar;
    }

    public boolean isFABVisible() {
        return mFABVisible;
    }

    @Override
    public String toString() {
        return mName;
    }
}
