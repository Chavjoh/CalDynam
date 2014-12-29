package ch.hesso.master.caldynam.ui.adapter;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.File;
import java.util.List;

import ch.hesso.master.caldynam.R;
import ch.hesso.master.caldynam.model.LoggingAdapterModel;
import ch.hesso.master.caldynam.util.ImageUtils;

public class LoggingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    public static final int TITLE = 0;
    public static final int ITEM = 1;
    public static final int ADD = 2;

    private List<LoggingAdapterModel> mData;
    private final Activity context;

    public static class TitleViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextView;

        public TitleViewHolder(View rootView) {
            super(rootView);
            mTextView = (TextView) rootView.findViewById(R.id.tv_logging_title_row);
        }
    }

    public static class AddViewHolder extends RecyclerView.ViewHolder {
        public Button mAddButton;

        public AddViewHolder(View rootView) {
            super(rootView);
            mAddButton = (Button) rootView.findViewById(R.id.btn_logging_add);
        }
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {
        public TextView mName;
        public TextView mCalories;
        public Button mDeleteButton;
        public ImageView mItemIcon;
        public ImageView mCalorieIcon;

        public ItemViewHolder(View rootView) {
            super(rootView);
            mName = (TextView) rootView.findViewById(R.id.tv_logging_item_name);
            mCalories = (TextView) rootView.findViewById(R.id.tv_logging_item_calories);
            mDeleteButton = (Button) rootView.findViewById(R.id.btn_logging_item_delete);
            mItemIcon = (ImageView) rootView.findViewById(R.id.iv_logging_item_icon);
            mCalorieIcon = (ImageView) rootView.findViewById(R.id.iv_logging_item_calorie_icon);
        }
    }

    public LoggingAdapter(Activity context, List<LoggingAdapterModel> data) {
        this.mData = data;
        this.context = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                      int viewType) {
        switch (viewType) {
            case TITLE: {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_logging_title_row, parent, false);
                return new TitleViewHolder(v);
            }
            case ITEM: {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_logging_item_row, parent, false);
                return new ItemViewHolder(v);
            }
            case ADD: {
                View v = LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.fragment_logging_add_row, parent, false);
                return new AddViewHolder(v);
            }
        }
        return new RecyclerView.ViewHolder(new View(parent.getContext())) {
        };

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof TitleViewHolder) {
            ((TitleViewHolder) holder).mTextView.setText(mData.get(position).get());
        }
        if (holder instanceof AddViewHolder) {
            ((AddViewHolder) holder).mAddButton.setText(mData.get(position).get());
            ((AddViewHolder) holder).mAddButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((LoggingAdapterModel.LoggingAdapterModelAdd) mData.get(position)).mCallback.onClick();
                }
            });
        }
        if (holder instanceof ItemViewHolder) {
            ((ItemViewHolder) holder).mName.setText(mData.get(position).get());
            ((ItemViewHolder) holder).mDeleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((LoggingAdapterModel.LoggingAdapterModelItem) mData.get(position)).mCallback.onClick();
                }
            });

            Object image = ((LoggingAdapterModel.LoggingAdapterModelItem) mData.get(position)).getImage();
            if (image != null && image instanceof String) {
                File imgFile = new File(context.getFilesDir(), (String) image);

                if (imgFile.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    ((ItemViewHolder) holder).mItemIcon.setImageBitmap(ImageUtils.getRoundedCornerBitmap(bitmap, 64));
                }
            }
            else {
                ((ItemViewHolder) holder).mItemIcon.setImageResource((int)image);
            }

            int calories = ((LoggingAdapterModel.LoggingAdapterModelItem) mData.get(position)).getCalories();
            ((ItemViewHolder) holder).mCalories.setText(calories + " calories");
            ((ItemViewHolder) holder).mCalorieIcon.setImageResource(calories > 0 ? R.drawable.ic_apple_48dp : R.drawable.ic_burn_48dp);
        }
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getItemViewType();
    }
}
