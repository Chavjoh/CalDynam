package ch.hesso.master.caldynam.ui.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import ch.hesso.master.caldynam.database.Food;
import ch.hesso.master.caldynam.util.LayoutUtils;

public class FoodSpinnerAdapter extends ArrayAdapter<Food> {

    private final static int LAYOUT = android.R.layout.simple_list_item_1;
    private final Activity context;
    private final List<Food> values;

    private static class ViewHolder {
        public TextView tvTitle;
    }

    public FoodSpinnerAdapter(Activity context, List<Food> values) {
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
        FoodSpinnerAdapter.ViewHolder holder;

        // Reuse views
        if (rowView == null) {
            LayoutInflater inflater = LayoutUtils.getInflater(context);
            rowView = inflater.inflate(LAYOUT, parent, false);

            holder = new FoodSpinnerAdapter.ViewHolder();
            holder.tvTitle = LayoutUtils.findView(rowView, android.R.id.text1);

            rowView.setTag(holder);
        }

        // Fill data
        holder = (FoodSpinnerAdapter.ViewHolder) rowView.getTag();

        Food food = values.get(position);
        holder.tvTitle.setText(food.getName());

        return rowView;
    }

}