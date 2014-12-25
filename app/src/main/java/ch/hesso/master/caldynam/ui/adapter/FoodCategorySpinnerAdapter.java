package ch.hesso.master.caldynam.ui.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ch.hesso.master.caldynam.R;
import ch.hesso.master.caldynam.database.Food;
import ch.hesso.master.caldynam.database.FoodCategory;

public class FoodCategorySpinnerAdapter extends ArrayAdapter<FoodCategory> {

    private final Context context;
    private final List<FoodCategory> values;

    public FoodCategorySpinnerAdapter(Context context, List<FoodCategory> values) {
        super(context, android.R.layout.simple_spinner_item, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return customView(position, parent);
    }

    @Override
    public View getDropDownView(int position, View convertView, ViewGroup parent) {
        return customView(position, parent);
    }

    public View customView(int position, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(android.R.layout.simple_spinner_item, parent, false);

        TextView tvTitle = (TextView) rowView.findViewById(android.R.id.text1);

        FoodCategory category = values.get(position);
        tvTitle.setText(category.getName());

        return rowView;
    }

}