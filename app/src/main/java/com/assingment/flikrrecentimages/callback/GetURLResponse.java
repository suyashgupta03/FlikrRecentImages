package com.assingment.flikrrecentimages.callback;

import com.assingment.flikrrecentimages.model.Response;

/**
 * Created by suyashg on 04/09/16.
 */

public interface GetURLResponse {
    public void getResponseofGetURL(Response response);
    public void errorResposeofGetURL(Exception e, Response response);
}
