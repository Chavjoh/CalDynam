package ch.hesso.master.caldynam.ui.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;

import ch.hesso.master.caldynam.R;
import ch.hesso.master.caldynam.database.Food;
import ch.hesso.master.caldynam.util.ImageUtils;

public class FoodAdapter extends ArrayAdapter<Food> {
    private final Context context;
    private final Food[] values;

    public FoodAdapter(Context context, Food[] values) {
        super(context, R.layout.list_row_food, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_row_food, parent, false);

        TextView tvTitle = (TextView) rowView.findViewById(R.id.tv_title);
        TextView tvDescription = (TextView) rowView.findViewById(R.id.tv_description);
        ImageView ivIcon = (ImageView) rowView.findViewById(R.id.iv_icon);

        Food food = values[position];
        tvTitle.setText(food.getName());
        tvDescription.setText(food.getCalorie() + " calories");

        File imgFile = new File(context.getFilesDir(), food.getImage());

        if (imgFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
            ivIcon.setImageBitmap(ImageUtils.getRoundedCornerBitmap(bitmap, 64));
        }

        return rowView;
    }
}