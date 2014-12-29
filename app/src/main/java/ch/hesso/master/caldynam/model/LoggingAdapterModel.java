package ch.hesso.master.caldynam.model;

import ch.hesso.master.caldynam.database.Logging;
import ch.hesso.master.caldynam.ui.adapter.LoggingAdapter;

public interface LoggingAdapterModel {
    public String get();

    public int getItemViewType();

    public class LoggingAdapterModelTitle implements LoggingAdapterModel {
        String mTitle;

        public LoggingAdapterModelTitle(String title) {
            mTitle = title;
        }

        @Override
        public String get() {
            return mTitle;
        }

        @Override
        public int getItemViewType() {
            return LoggingAdapter.TITLE;
        }
    }

    public class LoggingAdapterModelAdd implements LoggingAdapterModel {

        public interface LoggingAdapterModelAddCallback {
            public void onClick();
        }

        private String mTitle;
        public LoggingAdapterModelAddCallback mCallback;

        public LoggingAdapterModelAdd(String title, LoggingAdapterModelAddCallback callback) {
            mTitle = title;
            mCallback = callback;
        }

        @Override
        public String get() {
            return mTitle;
        }

        @Override
        public int getItemViewType() {
            return LoggingAdapter.ADD;
        }
    }

    public class LoggingAdapterModelItem implements LoggingAdapterModel {

        public interface LoggingAdapterModelItemDeleteCallback {
            public void onClick();
        }

        private Logging mItem;
        public LoggingAdapterModelItemDeleteCallback mCallback;

        public LoggingAdapterModelItem(Logging item, LoggingAdapterModelItemDeleteCallback callback) {
            mItem = item;
            mCallback = callback;
        }

        public Object getImage() {
            return (mItem.getFood() != null) ? mItem.getFood().getImage() : mItem.getWorkout().getImage();
        }

        public int getCalories() {
            return (mItem.getFood() != null) ? mItem.getFood().getCalorie() : -(int)(mItem.getWorkout().getCalorie() * mItem.getQuantity());
        }

        @Override
        public String get() {
            return (mItem.getFood() != null) ? mItem.getFood().getName() : mItem.getWorkout().getName();
        }

        @Override
        public int getItemViewType() {
            return LoggingAdapter.ITEM;
        }
    }
}
