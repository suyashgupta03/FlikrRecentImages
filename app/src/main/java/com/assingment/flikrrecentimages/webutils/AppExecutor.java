package com.assingment.flikrrecentimages.webutils;

/**
 * Created by suyashg on 03/09/16.
 */

import com.assingment.flikrrecentimages.callback.GetURLResponse;
import com.assingment.flikrrecentimages.callback.RecentPhotoResponseCallback;
import com.assingment.flikrrecentimages.model.Response;
import com.assingment.flikrrecentimages.utils.ServiceType;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AppExecutor implements GetURLResponse {

    //create a static object of AppExecutor
    private static AppExecutor instance = new AppExecutor();

    private ExecutorService executor = Executors.newSingleThreadExecutor();
    private RecentPhotoResponseCallback recentPhotoResponseCallback;
    private static final int AWAIT_TEMINATION_INTERVAL = 5;//secs

    public static AppExecutor getInstance() {
        if (instance == null) {
            instance = new AppExecutor();
        }
        return instance;
    }

    public void addPhotoServiceToQueue(String url, boolean isImageDownload, RecentPhotoResponseCallback recentPhotoResponseCallback, Response response) {
        this.recentPhotoResponseCallback = recentPhotoResponseCallback;
        this.queueTask(url, isImageDownload, response);
    }

    private void queueTask(String url, boolean isImageDownload, Response response){
        executor.submit(() -> {
            new DefaultClient(url, this, isImageDownload).executeRequest(response);
        });
    }

    private boolean isRunning() {
        return !executor.isTerminated();
    }

    @Override
    public void getResponseofGetURL(Response response) {
        switch (response.getServiceType()) {
            case RECENT_IMAGE:
                recentPhotoResponseCallback.handlePhotoServiceResponse(ServiceType.RECENT_IMAGE, response);
                break;
            case RECENT_RESPONSE:
                recentPhotoResponseCallback.handlePhotoServiceResponse(ServiceType.RECENT_RESPONSE, response);
                break;

        }
    }

    @Override
    public void errorResposeofGetURL(Exception e, Response response) {
        switch (response.getServiceType()) {
            case RECENT_IMAGE:
                recentPhotoResponseCallback.handlePhotoServiceError(ServiceType.RECENT_IMAGE, response, e);
                break;
            case RECENT_RESPONSE:
                recentPhotoResponseCallback.handlePhotoServiceResponse(ServiceType.RECENT_RESPONSE, response);
                break;

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
