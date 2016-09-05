package com.assingment.flikrrecentimages.model;

import android.graphics.Bitmap;

import com.assingment.flikrrecentimages.utils.ServiceType;

/**
 * Created by suyashg on 04/09/16.
 */

public class Response {
    private String url;// response came for this url
    private ServiceType serviceType;
    private FlikerURLResponse flikerURLResponse;
    private Bitmap bitmap;
    private boolean isImageResponse;

    public Response(ServiceType serviceType) {
        this.serviceType = serviceType;
    }

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

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public ServiceType getServiceType() {
        return serviceType;
    }

    public void setServiceType(ServiceType serviceType) {
        this.serviceType = serviceType;
    }
}
