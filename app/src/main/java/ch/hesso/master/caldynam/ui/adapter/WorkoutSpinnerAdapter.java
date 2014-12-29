package ch.hesso.master.caldynam.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ch.hesso.master.caldynam.database.Workout;
import ch.hesso.master.caldynam.util.LayoutUtils;

public class WorkoutSpinnerAdapter extends ArrayAdapter<Workout> {

    private final static int LAYOUT = android.R.layout.simple_list_item_1;
    private final Activity context;
    private final List<Workout> values;

    private static class ViewHolder {
        public TextView tvTitle;
    }

    public WorkoutSpinnerAdapter(Activity context, List<Workout> values) {
        super(context, LAYOUT, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return customView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return customView(position, convertView, parent);
    }

    public View customView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        WorkoutSpinnerAdapter.ViewHolder holder;

        // Reuse views
        if (rowView == null) {
            LayoutInflater inflater = LayoutUtils.getInflater(context);
            rowView = inflater.inflate(LAYOUT, parent, false);

            holder = new WorkoutSpinnerAdapter.ViewHolder();
            holder.tvTitle = LayoutUtils.findView(rowView, android.R.id.text1);

            rowView.setTag(holder);
        }

        // Fill data
        holder = (WorkoutSpinnerAdapter.ViewHolder) rowView.getTag();

        Workout workout = values.get(position);
        holder.tvTitle.setText(workout.getName());

        return rowView;
    }

}