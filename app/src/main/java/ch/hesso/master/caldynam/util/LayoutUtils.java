package ch.hesso.master.caldynam.util;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;

import ch.hesso.master.caldynam.Constants;

public class LayoutUtils {

    public static boolean isEmpty(EditText etText) {
        return etText.getText().toString().trim().length() == 0;
    }

    public static LayoutInflater getInflater(Activity context) {
        return (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T findView(Activity context, int id) {
        T view = null;

        View genericView = context.findViewById(id);

        try {
            view = (T) (genericView);
        } catch (Exception ex) {
            String message = "Can't cast view (" + id + ") to a " + view.getClass()
                    + ".  Is actually a " + genericView.getClass() + ".";
            Log.e(Constants.PROJECT_NAME, message);
            throw new ClassCastException(message);
        }

        return view;
    }

    @SuppressWarnings("unchecked")
    public static <T extends View> T findView(View parentView, int id) {
        T view = null;

        View genericView = parentView.findViewById(id);

        try {
            view = (T) (genericView);
        } catch (Exception ex) {
            String message = "Can't cast view (" + id + ") to a " + view.getClass()
                    + ".  Is actually a " + genericView.getClass() + ".";
            Log.e(Constants.PROJECT_NAME, message);
            throw new ClassCastException(message);
        }

        return view;
    }

}