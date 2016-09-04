package com.assingment.flikrrecentimages.model;

import android.graphics.Bitmap;

/**
 * Created by suyashg on 04/09/16.
 */

public class Response {
    private FlikerURLResponse flikerURLResponse;
    private Bitmap bitmap;
    private boolean isImageResponse;

    public FlikerURLResponse getFlikerURLResponse() {
        return flikerURLResponse;
    }

    public void setFlikerURLResponse(FlikerURLResponse flikerURLResponse) {
        this.flikerURLResponse = flikerURLResponse;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public boolean isImageResponse() {
        return isImageResponse;
    }

    public void setImageResponse(boolean imageResponse) {
        isImageResponse = imageResponse;
    }
}
