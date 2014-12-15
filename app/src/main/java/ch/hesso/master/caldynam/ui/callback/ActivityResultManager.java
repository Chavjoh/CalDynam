package ch.hesso.master.caldynam.ui.callback;

import android.content.Intent;

import java.util.HashMap;

import ch.hesso.master.caldynam.IntentTag;

public class ActivityResultManager {

    private static final HashMap<IntentTag, ActivityResultCallback> MAP;

    static {
        MAP = new HashMap<IntentTag, ActivityResultCallback>();
    }

    public static void registerCallback(IntentTag tag, ActivityResultCallback callback) {
        MAP.put(tag, callback);
    }

    public static void execute(IntentTag tag, int requestCode, int resultCode, Intent data) {
        ActivityResultCallback callback = MAP.get(tag);

        if (callback != null) {
            callback.onResult(requestCode, resultCode, data);
        }
    }

    public static void unregisterCallback(IntentTag tag) {
        MAP.remove(tag);
    }

    public static void clear() {
        MAP.clear();
    }

}
