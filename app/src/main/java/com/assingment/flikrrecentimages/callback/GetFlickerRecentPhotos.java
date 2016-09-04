package com.assingment.flikrrecentimages.callback;

import com.assingment.flikrrecentimages.model.FlikerURLResponse;

/**
 * Created by suyashg on 04/09/16.
 */

public interface GetFlickerRecentPhotos {
    public void getRecentImages(FlikerURLResponse flikerURLResponse);
    public void showRecentPhotosError(String errorMessage);
}
