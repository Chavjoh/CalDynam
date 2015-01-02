package ch.hesso.master.caldynam.ui.fragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Fragment;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TimePicker;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import ch.hesso.master.caldynam.MainActivity;
import ch.hesso.master.caldynam.R;
import ch.hesso.master.caldynam.database.Food;
import ch.hesso.master.caldynam.database.FoodCategory;
import ch.hesso.master.caldynam.database.Logging;
import ch.hesso.master.caldynam.database.Workout;
import ch.hesso.master.caldynam.model.LoggingAdapterModel;
import ch.hesso.master.caldynam.repository.FoodCategoryRepository;
import ch.hesso.master.caldynam.repository.FoodRepository;
import ch.hesso.master.caldynam.repository.LoggingRepository;
import ch.hesso.master.caldynam.repository.WorkoutRepository;
import ch.hesso.master.caldynam.ui.adapter.FoodCategorySpinnerAdapter;
import ch.hesso.master.caldynam.ui.adapter.FoodSpinnerAdapter;
import ch.hesso.master.caldynam.ui.adapter.LoggingAdapter;
import ch.hesso.master.caldynam.ui.adapter.WorkoutSpinnerAdapter;
import ch.hesso.master.caldynam.ui.helper.SlidingTabLayout;
import ch.hesso.master.caldynam.util.DateUtils;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link LoggingFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link LoggingFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class LoggingFragment extends Fragment {
    private OnFragmentInteractionListener mListener;

    private RecyclerView.Adapter mAdapter;
    private List<LoggingAdapterModel> mData;
    private Date mDate;

    private MaterialDialog mMaterialDialog;
    private ViewPager mViewPager;
    private FoodAddSubviewHolder mFoodAddSubviewHolder;
    private WorkoutAddSubviewHolder mWorkoutAddSubviewHolder;

    private static class FoodAddSubviewHolder {
        public Spinner mSpLoggingFoodCategory;
        public Spinner mSpLoggingFood;
    }

    private static class WorkoutAddSubviewHolder {
        public Spinner mSpLoggingWorkout;
        public EditText mEtWorkoutQuantity;
        public TimePicker mTpWorkoutQuantity;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment MealLogging.
     */
    public static LoggingFragment newInstance() {
        LoggingFragment fragment = new LoggingFragment();
        return fragment;
    }

    public LoggingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_logging, container, false);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mListener = (OnFragmentInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        ((MainActivity) activity).onSectionAttached(R.string.section_meal_logging);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mDate = new Date();
        findViews();
        refreshData();
    }

    @Override
    public void onResume() {
        super.onResume();
        Toolbar toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
        toolbar.setTitle(toolbar.getTitle() + " - " + DateUtils.dateToString(new Date()));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.logging, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_change_date) {
            Calendar calendar = new GregorianCalendar();
            calendar.setTimeInMillis(mDate.getTime());
            new DatePickerDialog(getActivity(),
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                            GregorianCalendar calendar = new GregorianCalendar();
                            calendar.set(year, monthOfYear, dayOfMonth);
                            mDate.setTime(calendar.getTimeInMillis());
                            refreshData();
                        }
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH))
                    .show();

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void refreshData() {
        List<Logging> loggings = LoggingRepository.getToday(getActivity(), mDate);
        mData.clear();
        mData.add(new LoggingAdapterModel.LoggingAdapterModelTitle(getString(R.string.morning)));
        mData.add(new LoggingAdapterModel.LoggingAdapterModelAdd(getString(R.string.add_recipe_workout), new LoggingAdapterModel.LoggingAdapterModelAdd.LoggingAdapterModelAddCallback() {
            @Override
            public void onClick() {
                addItem(DateUtils.DAY_PARTING.MORNING);
            }
        }));
        mData.add(new LoggingAdapterModel.LoggingAdapterModelTitle(getString(R.string.daytime)));
        mData.add(new LoggingAdapterModel.LoggingAdapterModelAdd(getString(R.string.add_recipe_workout), new LoggingAdapterModel.LoggingAdapterModelAdd.LoggingAdapterModelAddCallback() {
            @Override
            public void onClick() {
                addItem(DateUtils.DAY_PARTING.DAYTIME);
            }
        }));
        mData.add(new LoggingAdapterModel.LoggingAdapterModelTitle(getString(R.string.evening)));
        mData.add(new LoggingAdapterModel.LoggingAdapterModelAdd(getString(R.string.add_recipe_workout), new LoggingAdapterModel.LoggingAdapterModelAdd.LoggingAdapterModelAddCallback() {
            @Override
            public void onClick() {
                addItem(DateUtils.DAY_PARTING.EVENING);
            }
        }));
        int indexMorning = 1;
        int indexDay = 3;

        for (final Logging logging : loggings) {
            DateUtils.DAY_PARTING dayParting = DateUtils.dayParting(logging.getDate());
            LoggingAdapterModel loggingAdapter = new LoggingAdapterModel.LoggingAdapterModelItem(logging, new LoggingAdapterModel.LoggingAdapterModelItem.LoggingAdapterModelItemDeleteCallback() {
                @Override
                public void onClick() {
                    LoggingRepository.delete(getActivity(), logging.getId());
                    refreshData();
                }
            });
            if (dayParting == DateUtils.DAY_PARTING.MORNING) {
                mData.add(indexMorning, loggingAdapter);
            } else if (dayParting == DateUtils.DAY_PARTING.DAYTIME) {
                mData.add(indexDay, loggingAdapter);
            } else {
                mData.add(mData.size() - 1, loggingAdapter);
            }
            indexMorning++;
            indexDay++;
        }
        mAdapter.notifyDataSetChanged();
    }

    private void addItem(final DateUtils.DAY_PARTING dayParting) {
        View contentView = LayoutInflater.from(getActivity())
                .inflate(R.layout.fragment_logging_add_dialog, null);
        mViewPager = (ViewPager) contentView.findViewById(R.id.pager_logging_fragment);

        mViewPager.setAdapter(getViewPagerAdapter());
        SlidingTabLayout slidingTabLayout = (SlidingTabLayout) contentView.findViewById(R.id.sliding_tabs);
        slidingTabLayout.setViewPager(mViewPager);

        mMaterialDialog = new MaterialDialog(getActivity())
                .setTitle(getString(R.string.add_recipe_workout))
                .setContentView(contentView)
                .setPositiveButton(getString(R.string.add), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (createItem(dayParting)) {
                            mMaterialDialog.dismiss();
                            LoggingFragment.this.refreshData();
                        }
                    }
                })
                .setNegativeButton(R.string.cancel, new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mMaterialDialog.dismiss();
                    }
                });
        mMaterialDialog.show();
    }

    private boolean createItem(DateUtils.DAY_PARTING dayParting) {
        boolean isFood = mViewPager.getCurrentItem() == 0;
        if (isFood) {
            Food food = (Food) mFoodAddSubviewHolder.mSpLoggingFood.getSelectedItem();
            if (food != null) {
                Date date = DateUtils.dateWithDayParting(mDate, dayParting);
                Logging logging = new Logging(null, date, food.getId(), null, null);
                LoggingRepository.insertOrUpdate(getActivity(), logging);
                return true;
            } else {
                return false;
            }
        } else {
            Workout workout = (Workout) mWorkoutAddSubviewHolder.mSpLoggingWorkout.getSelectedItem();
            float quantity = -1;
            try {
                if (workout.getCalorie() == 1) {
                    quantity = Float.parseFloat(mWorkoutAddSubviewHolder.mEtWorkoutQuantity.getText().toString());
                }
                else {
                    quantity = mWorkoutAddSubviewHolder.mTpWorkoutQuantity.getCurrentHour() + mWorkoutAddSubviewHolder.mTpWorkoutQuantity.getCurrentMinute() / 60;
                }
            } catch (NumberFormatException e) {
                // Nothing now
            }
            if (workout != null && quantity > 0) {
                Date date = DateUtils.dateWithDayParting(mDate, dayParting);
                Logging logging = new Logging(null, date, null, workout.getId(), quantity);
                LoggingRepository.insertOrUpdate(getActivity(), logging);
                return true;
            } else {
                return false;
            }
        }
    }

    private PagerAdapter getViewPagerAdapter() {
        return new PagerAdapter() {
            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                View view = getAddDialogSubview(container, position);
                container.addView(view);
                return view;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }

            @Override
            public CharSequence getPageTitle(int position) {
                int title = position == 0 ? R.string.food : R.string.workout;
                return getString(title);
            }
        };
    }

    private View getAddDialogSubview(ViewGroup container, int position) {
        boolean isFood = position == 0;
        int ressource = isFood ? R.layout.fragment_logging_add_food_dialog : R.layout.fragment_logging_add_workout_dialog;
        View view = LayoutInflater.from(getActivity())
                .inflate(ressource, container, false);

        if (isFood) {
            mFoodAddSubviewHolder = new FoodAddSubviewHolder();
            mFoodAddSubviewHolder.mSpLoggingFoodCategory = (Spinner) view.findViewById(R.id.sp_logging_food_category);
            mFoodAddSubviewHolder.mSpLoggingFood = (Spinner) view.findViewById(R.id.sp_logging_food);

            List<FoodCategory> listFoodCategory = FoodCategoryRepository.getAll(getActivity());
            final List<Food> listFood = new ArrayList<>();
            FoodCategorySpinnerAdapter foodCategorySpinnerAdapter = new FoodCategorySpinnerAdapter(getActivity(), listFoodCategory);
            FoodSpinnerAdapter foodSpinnerAdapter = new FoodSpinnerAdapter(getActivity(), listFood);
            mFoodAddSubviewHolder.mSpLoggingFoodCategory.setAdapter(foodCategorySpinnerAdapter);
            mFoodAddSubviewHolder.mSpLoggingFood.setAdapter(foodSpinnerAdapter);

            mFoodAddSubviewHolder.mSpLoggingFoodCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    FoodCategory category = (FoodCategory) mFoodAddSubviewHolder.mSpLoggingFoodCategory.getSelectedItem();
                    listFood.clear();
                    listFood.addAll(FoodRepository.getAll(getActivity(), category));
                    ((FoodSpinnerAdapter) mFoodAddSubviewHolder.mSpLoggingFood.getAdapter()).notifyDataSetChanged();
                    mFoodAddSubviewHolder.mSpLoggingFood.setVisibility(listFood.size() > 0 ? View.VISIBLE : View.GONE);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) { /* Nothing */ }
            });
        } else {
            mWorkoutAddSubviewHolder = new WorkoutAddSubviewHolder();
            mWorkoutAddSubviewHolder.mSpLoggingWorkout = (Spinner) view.findViewById(R.id.sp_logging_workout);
            mWorkoutAddSubviewHolder.mEtWorkoutQuantity = (EditText) view.findViewById(R.id.et_workout_quantity);
            mWorkoutAddSubviewHolder.mTpWorkoutQuantity = (TimePicker) view.findViewById(R.id.tp_workout_quantity);
            mWorkoutAddSubviewHolder.mTpWorkoutQuantity.setIs24HourView(true);
            mWorkoutAddSubviewHolder.mTpWorkoutQuantity.setCurrentHour(1);
            mWorkoutAddSubviewHolder.mTpWorkoutQuantity.setCurrentMinute(0);
            List<Workout> listWorkout = WorkoutRepository.getAll(getActivity());
            WorkoutSpinnerAdapter workoutSpinnerAdapter = new WorkoutSpinnerAdapter(getActivity(), listWorkout);
            mWorkoutAddSubviewHolder.mSpLoggingWorkout.setAdapter(workoutSpinnerAdapter);
            mWorkoutAddSubviewHolder.mSpLoggingWorkout.setOnItemSelectedListener( new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    Workout workout = (Workout) mWorkoutAddSubviewHolder.mSpLoggingWorkout.getSelectedItem();
                    if (workout.getCalorie() == 1) {
                        mWorkoutAddSubviewHolder.mTpWorkoutQuantity.setVisibility(View.GONE);
                        mWorkoutAddSubviewHolder.mEtWorkoutQuantity.setVisibility(View.VISIBLE);
                    }
                    else {
                        mWorkoutAddSubviewHolder.mTpWorkoutQuantity.setVisibility(View.VISIBLE);
                        mWorkoutAddSubviewHolder.mEtWorkoutQuantity.setVisibility(View.GONE);
                    }
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) { /* Nothing */ }
            });
        }
        return view;
    }

    public void findViews() {
        RecyclerView recyclerView = (RecyclerView) getActivity().findViewById(R.id.logging_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mData = new ArrayList<>();
        mAdapter = new LoggingAdapter(getActivity(), mData);
        recyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html">
     * Communicating with Other Fragments</a> for more information.
     * </p>
     */
    public interface OnFragmentInteractionListener {

    }

}
