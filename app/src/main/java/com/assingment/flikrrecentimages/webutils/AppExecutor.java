package com.assingment.flikrrecentimages.webutils;

/**
 * Created by suyashg on 03/09/16.
 */

import com.assingment.flikrrecentimages.callback.GetFlickerRecentPhotos;
import com.assingment.flikrrecentimages.callback.GetURLResponse;
import com.assingment.flikrrecentimages.callback.RefreshImageCallback;
import com.assingment.flikrrecentimages.model.Photo;
import com.assingment.flikrrecentimages.model.Response;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AppExecutor implements GetURLResponse {

    //create a static object of AppExecutor
    private static AppExecutor instance = new AppExecutor();

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private RefreshImageCallback refreshImageCallback;
    private GetFlickerRecentPhotos recentPhotosCallback;
    private static final int AWAIT_TEMINATION_INTERVAL = 5;//secs

    public static AppExecutor getInstance() {
        if (instance == null) {
            instance = new AppExecutor();
        }
        return instance;
    }

    public void addPhotoRequestToExecutor(RefreshImageCallback refreshImageCallback, List<Photo> photos) {
        this.refreshImageCallback = refreshImageCallback;
        for (Photo photo : photos) {
            this.queueTask(photo.getUrl_n(), true);
        }
    }

    private void queueTask(String url, boolean isImageDownload){
        executor.submit(() -> {
            new DefaultClient(url, this, isImageDownload).executeRequest();
        });
    }

    public void addRecentRequestToExecutor(GetFlickerRecentPhotos recentPhotosCallback, String url) {
        this.recentPhotosCallback = recentPhotosCallback;
        this.queueTask(url, false);
    }

    private boolean isRunning() {
        return !executor.isTerminated();
    }

    @Override
    public void getResponseofGetURL(Response response) {
        if (response.isImageResponse()) {
            refreshImageCallback.refreshTheImage(response.getBitmap());
        } else {
            recentPhotosCallback.getRecentImages(response.getFlikerURLResponse());
        }
    }

    @Override
    public void errorResposeofGetURL(Exception e, Response response) {
        if (response.isImageResponse()) {
            refreshImageCallback.showImageDownloadError(e.getMessage());
        } else {
            recentPhotosCallback.showRecentPhotosError(e.getMessage());
        }
    }

    public void stop() {
        try {
            executor.shutdown();
            executor.awaitTermination(AWAIT_TEMINATION_INTERVAL, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            if (!executor.isTerminated()) {
                executor.shutdownNow();
            }
        }
        instance = null;
    }
}
