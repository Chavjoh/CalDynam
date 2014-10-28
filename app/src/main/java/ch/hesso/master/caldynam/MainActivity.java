package ch.hesso.master.caldynam;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;

import ch.hesso.master.caldynam.ui.fragment.FoodCatalogFragment;
import ch.hesso.master.caldynam.ui.fragment.LoggingFragment;
import ch.hesso.master.caldynam.ui.fragment.NavigationDrawerFragment;
import ch.hesso.master.caldynam.ui.fragment.SummaryFragment;
import ch.hesso.master.caldynam.ui.fragment.WeightMeasurementFragment;

public class MainActivity extends Activity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks,
        SummaryFragment.OnFragmentInteractionListener,
        WeightMeasurementFragment.OnFragmentInteractionListener,
        LoggingFragment.OnFragmentInteractionListener,
        FoodCatalogFragment.OnFragmentInteractionListener {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = null;

        switch (position) {
            case 0:
                fragment = SummaryFragment.newInstance();
                break;

            case 1:
                fragment = WeightMeasurementFragment.newInstance();
                break;

            case 2:
                fragment = LoggingFragment.newInstance();
                break;

            case 3:
                fragment = FoodCatalogFragment.newInstance();
                break;
        }

        fragmentManager.beginTransaction().replace(R.id.container, fragment).commit();

        // Replace current menu with the fragment menu
        this.invalidateOptionsMenu();
    }

    public void onSectionAttached(int resourceId) {
        mTitle = getString(resourceId);
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            //getMenuInflater().inflate(R.menu.main, menu);
            restoreActionBar();
            return true;
        }

        return super.onCreateOptionsMenu(menu);
    }

    /**
     * Handle action bar item clicks here. The action bar will
     * automatically handle clicks on the Home/Up button, so long
     * as you specify a parent activity in AndroidManifest.xml.
     *
     * @param item
     * @return
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
