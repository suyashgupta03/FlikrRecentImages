package com.assingment.flikrrecentimages.callback;

import android.graphics.Bitmap;

/**
 * Created by suyashg on 04/09/16.
 */

public interface RefreshImageCallback {

    public void refreshTheImage(Bitmap bitmap);
    public void showImageDownloadError(String errorMessage);
}
