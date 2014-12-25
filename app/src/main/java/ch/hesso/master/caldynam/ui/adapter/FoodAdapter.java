package ch.hesso.master.caldynam.ui.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import ch.hesso.master.caldynam.R;
import ch.hesso.master.caldynam.database.Food;
import ch.hesso.master.caldynam.util.ImageUtils;
import ch.hesso.master.caldynam.util.LayoutUtils;

public class FoodAdapter extends ArrayAdapter<Food> {

    private final static int LAYOUT = R.layout.list_row_food;
    private final Activity context;
    private final List<Food> values;

    private static class ViewHolder {
        public TextView tvTitle;
        public TextView tvDescription;
        public ImageView ivIcon;
    }

    public FoodAdapter(Activity context, List<Food> values) {
        super(context, LAYOUT, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View rowView = convertView;
        FoodAdapter.ViewHolder holder;

        // Reuse views
        if (rowView == null) {
            LayoutInflater inflater = LayoutUtils.getInflater(context);
            rowView = inflater.inflate(LAYOUT, parent, false);

            holder = new FoodAdapter.ViewHolder();
            holder.tvTitle = LayoutUtils.findView(rowView, R.id.tv_title);
            holder.tvDescription = LayoutUtils.findView(rowView, R.id.tv_description);
            holder.ivIcon = LayoutUtils.findView(rowView, R.id.iv_icon);

            rowView.setTag(holder);
        }

        // Fill data
        holder = (FoodAdapter.ViewHolder) rowView.getTag();

        Food food = values.get(position);
        holder.tvTitle.setText(food.getName());
        holder.tvDescription.setText(food.getCalorie() + " calories");

        File imgFile = new File(context.getFilesDir(), food.getImage());

        if (imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            holder.ivIcon.setImageBitmap(ImageUtils.getRoundedCornerBitmap(bitmap, 64));
        }

        return rowView;
    }

    public void setData(List<Food> listFood) {
        this.clear();
        this.addAll(listFood);
    }
}