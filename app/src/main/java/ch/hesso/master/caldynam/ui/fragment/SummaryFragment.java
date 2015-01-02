package ch.hesso.master.caldynam.ui.fragment;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.hesso.master.caldynam.IntentTag;
import ch.hesso.master.caldynam.MainActivity;
import ch.hesso.master.caldynam.R;
import ch.hesso.master.caldynam.database.Food;
import ch.hesso.master.caldynam.database.Logging;
import ch.hesso.master.caldynam.database.Weight;
import ch.hesso.master.caldynam.repository.FoodRepository;
import ch.hesso.master.caldynam.repository.LoggingRepository;
import ch.hesso.master.caldynam.repository.WeightRepository;
import ch.hesso.master.caldynam.util.LayoutUtils;
import lecho.lib.hellocharts.model.ArcValue;
import lecho.lib.hellocharts.model.PieChartData;
import lecho.lib.hellocharts.util.Utils;
import lecho.lib.hellocharts.view.PieChartView;
import me.drakeet.materialdialog.MaterialDialog;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link SummaryFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link SummaryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SummaryFragment extends Fragment {

    public static final String WEIGHT_GOAL_KEY = "ch.hesso.master.caldynam.weightGoal";
    public static final String WORKOUT_GOAL_KEY = "ch.hesso.master.caldynam.workoutGoal";
    public static final String FOOD_GOAL_KEY = "ch.hesso.master.caldynam.foodGoal";

    private OnFragmentInteractionListener mListener;

    private TextView mTvWeight;
    private PieChartView mPieChartWorkout;
    private PieChartView mPieChartFood;

    private Float mWeightGoal;
    private Integer mWorkoutGoal;
    private Integer mFoodGoal;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment SummaryFragment.
     */
    public static SummaryFragment newInstance() {
        SummaryFragment fragment = new SummaryFragment();
        return fragment;
    }

    public SummaryFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_summary, container, false);

        mTvWeight = LayoutUtils.findView(view, R.id.tv_summary_weight);
        mPieChartWorkout = LayoutUtils.findView(view, R.id.summary_pie_chart_workout);
        mPieChartFood = LayoutUtils.findView(view, R.id.summary_pie_chart_food);

        fetchGoals();
        setupWeightInfo();
        setupCharts();

        return view;
    }

    private void fetchGoals() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mWeightGoal = sharedPreferences.getFloat(WEIGHT_GOAL_KEY, 0);
        mWorkoutGoal = sharedPreferences.getInt(WORKOUT_GOAL_KEY, 0);
        mFoodGoal = sharedPreferences.getInt(FOOD_GOAL_KEY, 0);
    }

    private void setupWeightInfo() {
        List<Weight> weights = WeightRepository.getAllLimit(getActivity(), 1);
        if (weights.size() > 0) {
            float weight = weights.get(0).getWeight();
            if (Math.abs(mWeightGoal - weight) < 0.1) {
                mTvWeight.setText(String.format(getString(R.string.perfect_weight), weight));
            }
            else {
                int textRessourceID = weight > mWeightGoal ? R.string.need_lose_weight_info : R.string.need_take_weight_info;
                mTvWeight.setText(String.format(getString(textRessourceID), weight, mWeightGoal, Math.abs(mWeightGoal - weight)));
            }
        } else {
            mTvWeight.setText(getString(R.string.no_weight_info));
        }
    }

    private void setupCharts() {
        float totalCaloriesFood = 0;
        float totalCaloriesWorkout = 0;
        List<Logging> loggings = LoggingRepository.getToday(getActivity(), new Date());
        for (Logging logging : loggings) {
            if (logging.getFood() != null) {
                totalCaloriesFood += logging.getFood().getCalorie();
            } else {
                totalCaloriesWorkout += logging.getQuantity() * logging.getWorkout().getCalorie();
            }
        }
        List<ArcValue> valuesFood = new ArrayList<>();
        ArcValue value = new ArcValue(0, totalCaloriesFood <= mFoodGoal ? Utils.COLOR_GREEN : Utils.COLOR_RED);
        value.setTarget(totalCaloriesFood);
        valuesFood.add(value);
        value = new ArcValue(mFoodGoal);
        value.setTarget(Math.max(mFoodGoal - totalCaloriesFood, 0));
        valuesFood.add(value);
        PieChartData dataFood = new PieChartData(valuesFood);
        dataFood.setHasLabels(true);
        dataFood.setHasLabelsOnlyForSelected(true);
        dataFood.setHasCenterCircle(true);
        dataFood.setCenterText1(getString(R.string.food));
        dataFood.setCenterText1FontSize(Utils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                (int) getResources().getDimension(R.dimen.pie_chart_text1_size)));
        dataFood.setCenterText2((int) totalCaloriesFood + " " + getString(R.string.cal));
        dataFood.setCenterText2FontSize(Utils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                (int) getResources().getDimension(R.dimen.pie_chart_text2_size)));
        mPieChartFood.setPieChartData(dataFood);

        List<ArcValue> valuesWorkout = new ArrayList<>();
        value = new ArcValue(0, Utils.COLOR_GREEN);
        value.setTarget(totalCaloriesWorkout);
        valuesWorkout.add(value);
        value = new ArcValue(mWorkoutGoal, Utils.COLOR_RED);
        value.setTarget(Math.max(mWorkoutGoal - totalCaloriesWorkout, 0));
        valuesWorkout.add(value);
        PieChartData dataWorkout = new PieChartData(valuesWorkout);
        dataWorkout.setHasLabels(true);
        dataWorkout.setHasLabelsOnlyForSelected(true);
        dataWorkout.setHasCenterCircle(true);
        dataWorkout.setCenterText1(getString(R.string.workout));
        dataWorkout.setCenterText1FontSize(Utils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                (int) getResources().getDimension(R.dimen.pie_chart_text1_size)));
        dataWorkout.setCenterText2((int) totalCaloriesWorkout + " " + getString(R.string.cal));
        dataWorkout.setCenterText2FontSize(Utils.px2sp(getResources().getDisplayMetrics().scaledDensity,
                (int) getResources().getDimension(R.dimen.pie_chart_text2_size)));
        mPieChartWorkout.setPieChartData(dataWorkout);

        mPieChartFood.setChartRotationEnabled(false);
        mPieChartWorkout.setChartRotationEnabled(false);
        mPieChartFood.setChartRotation(270, false);
        mPieChartWorkout.setChartRotation(270, false);
        mPieChartFood.startDataAnimation(1000);
        mPieChartWorkout.startDataAnimation(1000);
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

        ((MainActivity) activity).onSectionAttached(R.string.section_summary);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.summary, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_edit_profil) {
            View contentView = LayoutInflater.from(getActivity())
                    .inflate(R.layout.fragment_summary_edit_profile_dialog, null);
            final EditText etWeightGoal = (EditText) contentView.findViewById(R.id.et_edit_profile_dialog_weight_goal);
            final EditText etWorkoutGoal = (EditText) contentView.findViewById(R.id.et_edit_profile_dialog_workout_goal);
            final EditText etFoodGoal = (EditText) contentView.findViewById(R.id.et_edit_profile_dialog_food_goal);

            etWeightGoal.setText(mWeightGoal.toString());
            etWorkoutGoal.setText(mWorkoutGoal.toString());
            etFoodGoal.setText(mFoodGoal.toString());

            final MaterialDialog materialDialog = new MaterialDialog(getActivity());
            materialDialog.setTitle(getString(R.string.edit_profile))
                    .setContentView(contentView)
                    .setPositiveButton(getString(R.string.abc_action_mode_done), new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    float weightGoal = -1;
                                    int workoutGoal = -1;
                                    int foodGoal = -1;
                                    try {
                                        weightGoal = Float.parseFloat(etWeightGoal.getText().toString());
                                    } catch (NumberFormatException e) {
                                        // Nothing
                                    }
                                    try {
                                        workoutGoal = Integer.parseInt(etWorkoutGoal.getText().toString());
                                    } catch (NumberFormatException e) {
                                        // Nothing
                                    }
                                    try {
                                        foodGoal = Integer.parseInt(etFoodGoal.getText().toString());
                                    } catch (NumberFormatException e) {
                                        // Nothing
                                    }
                                    SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
                                    SharedPreferences.Editor editor = sharedPreferences.edit();
                                    if (weightGoal > 0) {
                                        editor.putFloat(WEIGHT_GOAL_KEY, weightGoal);
                                    }
                                    if (workoutGoal > 0) {
                                        editor.putInt(WORKOUT_GOAL_KEY, workoutGoal);
                                    }
                                    if (foodGoal > 0) {
                                        editor.putInt(FOOD_GOAL_KEY, foodGoal);
                                    }
                                    editor.apply();
                                    fetchGoals();
                                    setupWeightInfo();
                                    setupCharts();

                                    materialDialog.dismiss();
                                }
                            }

                    )
                    .setNegativeButton(R.string.cancel, new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    materialDialog.dismiss();
                                }
                            }

                    );
            materialDialog.show();
            return true;
        }

        return super.onOptionsItemSelected(item);
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
