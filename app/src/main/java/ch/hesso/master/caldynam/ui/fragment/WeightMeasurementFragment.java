package ch.hesso.master.caldynam.ui.fragment;

import android.app.Activity;
import android.graphics.Point;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.hesso.master.caldynam.MainActivity;
import ch.hesso.master.caldynam.R;
import ch.hesso.master.caldynam.database.Weight;
import ch.hesso.master.caldynam.repository.WeightRepository;
import ch.hesso.master.caldynam.util.ToastUtils;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.ValueShape;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.Utils;
import lecho.lib.hellocharts.view.LineChartView;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link WeightMeasurementFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link WeightMeasurementFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class WeightMeasurementFragment extends Fragment {

    private OnFragmentInteractionListener mListener;

    private int numberOfMeasure;

    private List<PointValue> listPoint;
    private List<Line> listLine;
    private Line weightLine;

    private LineChartView chart;
    private LineChartData data;
    private float maxWeight;

    private Button weightSubmit;
    private EditText weightValue;

    /**
     * Factory used to create a new instance of this fragment.
     *
     * @return A new instance of fragment WeightMeasurement.
     */
    public static WeightMeasurementFragment newInstance() {
        WeightMeasurementFragment fragment = new WeightMeasurementFragment();
        return fragment;
    }

    public WeightMeasurementFragment() {
        listPoint = new ArrayList<PointValue>();
        listLine = new ArrayList<Line>();
        maxWeight = 0;
        numberOfMeasure = 10;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_weight_measurement, container, false);

        // Get the chart and configure it
        chart = (LineChartView) rootView.findViewById(R.id.chart);
        chart.setOnValueTouchListener(new ValueTouchListener());
        chart.setViewportCalculationEnabled(false);
        chart.setZoomEnabled(false);

        configureChart();
        initializeData();
        refreshData();

        // Get the weight field
        weightValue = (EditText) rootView.findViewById(R.id.weight);

        // Get the button and add action listener
        weightSubmit = (Button) rootView.findViewById(R.id.submit);
        weightSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Weight weight = new Weight();
                weight.setWeight(Float.valueOf(weightValue.getText().toString()));
                weight.setDate(new Date());

                WeightRepository.insertOrUpdate(getActivity(), weight);
                refreshData();
            }
        });

        return rootView;
    }

    private void configureChart() {

        weightLine = new Line(listPoint);
        weightLine.setColor(Utils.COLORS[0]);
        weightLine.setShape(ValueShape.CIRCLE);
        weightLine.setCubic(false);
        weightLine.setFilled(true);
        weightLine.setHasLabels(true);
        weightLine.setHasLabelsOnlyForSelected(true);
        weightLine.setHasLines(true);
        weightLine.setHasPoints(true);
        listLine.add(weightLine);

        data = new LineChartData(listLine);

        Axis axisY = new Axis().setHasLines(true);
        axisY.setName("Weight");
        data.setAxisXBottom(null);
        data.setAxisYLeft(axisY);

        chart.setLineChartData(data);
    }

    private void initializeData() {

        for (int i = 0; i < numberOfMeasure; i++) {
            listPoint.add(new PointValue(i, 0));
        }
    }

    private void refreshData() {

        int i = numberOfMeasure - 1;

        List<Weight> listWeight = WeightRepository.getAllLimit(getActivity(), 10);

        float sum = 0f;

        for (Weight weight : listWeight) {
            float currentWeight = weight.getWeight();

            if (currentWeight > maxWeight) {
                maxWeight = currentWeight;
            }

            sum += currentWeight;
        }

        float average = sum / listWeight.size();

        for (Weight weight : listWeight) {
            PointValue currentPoint = listPoint.get(i);
            currentPoint.set(i, average);
            currentPoint.setTarget(i, weight.getWeight());
            currentPoint.setLabel(weight.getDate().toString().toCharArray()); // TODO: Format
            i--;
        }

        while (i >= 0) {
            listPoint.get(i).set(i, average);
            i--;
        }

        configureViewport();
        chart.startDataAnimation(700);
    }

    private void configureViewport() {
        final Viewport v = new Viewport();
        v.left = 0;
        v.bottom = 30;
        v.right = listPoint.size() - 1;
        v.top = maxWeight + 30;

        chart.setMaximumViewport(v);
        chart.setCurrentViewport(v, false);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.weight, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_reset) {
            WeightRepository.deleteAll(getActivity());
            ToastUtils.toast(getActivity(), "All weight measure are deleted.");
            return true;
        }

        return super.onOptionsItemSelected(item);
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

        ((MainActivity) activity).onSectionAttached(R.string.section_weight_measurement);
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

    private class ValueTouchListener implements LineChartView.LineChartOnValueTouchListener {

        @Override
        public void onValueTouched(int selectedLine, int selectedValue, PointValue value) {
            ToastUtils.toast(getActivity(), value.getLabel() + " : " + value.getY());

        }

        @Override
        public void onNothingTouched() {
            // Just do nothing
        }

    }

}
