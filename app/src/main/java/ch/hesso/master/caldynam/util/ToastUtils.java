package ch.hesso.master.caldynam.util;

import android.content.Context;
import android.widget.Toast;

public class ToastUtils {

    public static Toast toast(Context context, String message) {
        return toast(context, message, false);
    }

    public static Toast toast(Context context, String message, boolean longLength) {
        final Toast toast;
        if (longLength) {
            toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        } else {
            toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        }
        toast.show();
        return toast;
    }
}