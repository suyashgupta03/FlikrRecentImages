package com.assingment.flikrrecentimages.callback;

import com.assingment.flikrrecentimages.model.Response;
import com.assingment.flikrrecentimages.utils.ServiceType;

/**
 * Created by suyashg on 05/09/16.
 */

public interface RecentPhotoResponseCallback {
    public void handlePhotoServiceResponse(ServiceType serviceType, Response response);
    public void handlePhotoServiceError(ServiceType serviceType, Response response, Exception e);
}
