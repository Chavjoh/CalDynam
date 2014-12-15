package ch.hesso.master.caldynam.ui.callback;

import android.content.Intent;

public interface ActivityResultCallback {

    public void onResult(int requestCode, int resultCode, Intent data);

}
