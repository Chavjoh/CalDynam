package ch.hesso.master.caldynam;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Outline;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.Toast;

import ch.hesso.master.caldynam.ui.fragment.FoodAddFragment;
import ch.hesso.master.caldynam.ui.fragment.FoodCatalogFragment;
import ch.hesso.master.caldynam.ui.fragment.FoodViewFragment;
import ch.hesso.master.caldynam.ui.fragment.LoggingFragment;
import ch.hesso.master.caldynam.ui.fragment.NavigationDrawerFragment;
import ch.hesso.master.caldynam.ui.fragment.SummaryFragment;
import ch.hesso.master.caldynam.ui.fragment.WeightMeasurementFragment;
import me.drakeet.materialdialog.MaterialDialog;

public class MainActivity extends ActionBarActivity implements
        NavigationDrawerFragment.NavigationDrawerCallbacks,
        SummaryFragment.OnFragmentInteractionListener,
        WeightMeasurementFragment.OnFragmentInteractionListener,
        LoggingFragment.OnFragmentInteractionListener,
        FoodCatalogFragment.OnFragmentInteractionListener,
        FoodAddFragment.OnFragmentInteractionListener,
        FoodViewFragment.OnFragmentInteractionListener {

    private Fragment fragment = null;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #updateToolbar()}.
     */
    private CharSequence mTitle;

    private Toolbar mToolbar;

    private View mFabButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Handle Toolbar
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);


        // Handle different Drawer States :D
//        mDrawerLayout.setDrawerListener(mActionBarDrawerToggle);

        // Fab Button
        mFabButton = findViewById(R.id.fab_button);
        mFabButton.setOnClickListener(fabClickListener);

        mFabButton.setOutlineProvider(new ViewOutlineProvider() {
            @Override
            public void getOutline(View view, Outline outline) {
                int size = getResources().getDimensionPixelSize(R.dimen.fab_size);
                outline.setOval(0, 0, size, size);
            }
        });

        mNavigationDrawerFragment = (NavigationDrawerFragment) getFragmentManager().findFragmentById(R.id.navigation_drawer);

        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout)
        );
        updateToolbar();
        mTitle = getTitle();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {

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

        getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        loadFragment(fragment, false);
    }

    View.OnClickListener fabClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            Toast.makeText(MainActivity.this, "New data", Toast.LENGTH_SHORT).show();
        }
    };

    public void onSectionAttached(int resourceId) {
        mTitle = (resourceId != 0) ? getString(resourceId) : null;
    }

    public void updateToolbar() {
        if (mTitle != null) {
            mToolbar.setTitle(mTitle);
        }
        resizeToolbar(mNavigationDrawerFragment.isToolbarLarge() ? 1.0f : 0.0f);
        mFabButton.setAlpha(mNavigationDrawerFragment.isFABVisible()  ? 1.0f : 0.0f);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            if (fragment != null) {
                fragment.onCreateOptionsMenu(menu, getMenuInflater());
            }
            //getMenuInflater().inflate(R.menu.main, menu);
            updateToolbar();
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

        fragment.onOptionsItemSelected(item);

        if (id == R.id.action_about) {
            showAboutDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.d(Constants.PROJECT_NAME, "Popping backstack");
            fm.popBackStackImmediate();
            this.fragment = getActiveFragment();
        } else {
            Log.d(Constants.PROJECT_NAME, "Nothing on backstack, calling super");
            super.onBackPressed();
        }
    }

    private void showAboutDialog() {
        View contentView = LayoutInflater.from(this)
                .inflate(R.layout.fragment_about_dialog, null);
        final MaterialDialog aboutDialog = new MaterialDialog(this);
        aboutDialog
                .setContentView(contentView)
                .setPositiveButton(getString(R.string.ok), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        aboutDialog.dismiss();
                    }
                });
        aboutDialog.show();
    }

    public Fragment getActiveFragment() {
        if (getFragmentManager().getBackStackEntryCount() == 0) {
            return null;
        }
        String tag = getFragmentManager()
                .getBackStackEntryAt(getFragmentManager().getBackStackEntryCount() - 1)
                .getName();
        return getFragmentManager().findFragmentByTag(tag);
    }

    public void loadFragment(Fragment fragment) {
        loadFragment(fragment, true);
    }

    public void loadFragment(Fragment fragment, boolean addToBackStack) {
        this.fragment = fragment;

        String tag = fragment.getClass().getSimpleName();

        final FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(R.id.container, this.fragment, tag);

        if (addToBackStack) {
            Log.d("Fragment", tag);
            ft.addToBackStack(tag);
        }

        ft.commit();

        // Replace current menu with the fragment menu
        this.invalidateOptionsMenu();
    }

    public void resizeToolbar(float offset) {
        float minSize = mToolbar.getMinimumHeight();
        float maxSize = getResources().getDimension(R.dimen.toolbar_height_large);
        ViewGroup.LayoutParams layout = mToolbar.getLayoutParams();
        layout.height = (int) (minSize + (maxSize - minSize) * offset);
        mToolbar.requestLayout();
    }

    public View getAddButton() {
        return mFabButton;
    }


    /**
     * an animation for resizing the view.
     */
    private class ResizeAnimation extends Animation {
        private View mView;
        private float mToHeight;
        private float mFromHeight;

        private float mToWidth;
        private float mFromWidth;

        public ResizeAnimation(View v, float fromWidth, float fromHeight, float toWidth, float toHeight) {
            mToHeight = toHeight;
            mToWidth = toWidth;
            mFromHeight = fromHeight;
            mFromWidth = fromWidth;
            mView = v;
            setDuration(300);
        }

        @Override
        protected void applyTransformation(float interpolatedTime, Transformation t) {
            float height = (mToHeight - mFromHeight) * interpolatedTime + mFromHeight;
            float width = (mToWidth - mFromWidth) * interpolatedTime + mFromWidth;
            ViewGroup.LayoutParams p = mView.getLayoutParams();
            p.height = (int) height;
            p.width = (int) width;
            mView.requestLayout();
        }
    }
}