package com.assingment.flikrrecentimages.callback;

import android.graphics.Bitmap;

import com.assingment.flikrrecentimages.model.Photo;

/**
 * Created by suyashg on 05/09/16.
 */

public interface BitmapCacheCallback {

    public Bitmap getBitmapFromMemCache(String key);
    public void downloadBitmapForView(Photo photo);
}
