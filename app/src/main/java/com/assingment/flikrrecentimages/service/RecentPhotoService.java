package com.assingment.flikrrecentimages.service;

import com.assingment.flikrrecentimages.callback.GetFlickerRecentPhotos;
import com.assingment.flikrrecentimages.callback.RecentPhotoResponseCallback;
import com.assingment.flikrrecentimages.callback.RefreshImageCallback;
import com.assingment.flikrrecentimages.model.Photo;
import com.assingment.flikrrecentimages.model.Response;
import com.assingment.flikrrecentimages.utils.ServiceType;
import com.assingment.flikrrecentimages.webutils.AppExecutor;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by suyashg on 05/09/16.
 */

public class RecentPhotoService implements RecentPhotoResponseCallback {

    private RefreshImageCallback refreshImageCallback;
    private GetFlickerRecentPhotos recentPhotosCallback;
    private AppExecutor appExecutor;
    private Map<String, Photo> photosProcessed;

    public RecentPhotoService() {
        photosProcessed = new HashMap<>();
        appExecutor = AppExecutor.getInstance();
    }

    public void processPhotoRequest(RefreshImageCallback refreshImageCallback, List<Photo> photos) {
        this.refreshImageCallback = refreshImageCallback;
        for (Photo photo : photos) {
            photosProcessed.put(photo.getUrl_n(), photo);
            appExecutor.addPhotoServiceToQueue(photo.getUrl_n(), true, this, new Response(ServiceType.RECENT_IMAGE));
        }
    }

    public void processPhotoRequest(RefreshImageCallback refreshImageCallback, Photo photo) {
        this.refreshImageCallback = refreshImageCallback;
        photosProcessed.put(photo.getUrl_n(), photo);
        appExecutor.addPhotoServiceToQueue(photo.getUrl_n(), true, this, new Response(ServiceType.RECENT_IMAGE));
    }

    public void processRecentRequest(GetFlickerRecentPhotos recentPhotosCallback, String url) {
        this.recentPhotosCallback = recentPhotosCallback;
        appExecutor.addPhotoServiceToQueue(url, false, this, new Response(ServiceType.RECENT_RESPONSE));
    }

    @Override
    public void handlePhotoServiceResponse(ServiceType serviceType, Response response) {
        if (response.isImageResponse()) {
            refreshImageCallback.refreshTheImage(response.getUrl(), response.getBitmap());
        } else {
            recentPhotosCallback.getRecentImages(response.getFlikerURLResponse());
        }
    }

    @Override
    public void handlePhotoServiceError(ServiceType serviceType, Response response, Exception e) {
        if (response.isImageResponse()) {
            refreshImageCallback.showImageDownloadError(e.getMessage());
        } else {
            recentPhotosCallback.showRecentPhotosError(e.getMessage());
        }
    }

    public Photo getProcessedPhotoByUrl(String url) {
        return photosProcessed.get(url);
    }

    public void invalidate() {
        appExecutor.stop();
        photosProcessed.clear();
    }
}
