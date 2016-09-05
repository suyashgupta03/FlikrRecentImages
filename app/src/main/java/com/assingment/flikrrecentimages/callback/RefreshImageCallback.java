package com.assingment.flikrrecentimages.callback;

import android.graphics.Bitmap;

/**
 * Created by suyashg on 04/09/16.
 */

public interface RefreshImageCallback {

    public void refreshTheImage(String url, Bitmap bitmap);
    public void showImageDownloadError(String errorMessage);
}
