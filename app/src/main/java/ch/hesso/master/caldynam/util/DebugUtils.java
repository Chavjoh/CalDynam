package ch.hesso.master.caldynam.util;

import android.util.Log;

import java.io.File;

public class DebugUtils {

    public static void listFile(File directory) {
        File file[] = directory.listFiles();

        Log.d("Files", "Size : " + file.length);

        for (int i=0; i < file.length; i++)
        {
            Log.d("Files", "FileName : " + file[i].getName());
        }
    }
}
