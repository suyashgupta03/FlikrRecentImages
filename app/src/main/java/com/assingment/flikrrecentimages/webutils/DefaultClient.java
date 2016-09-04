package com.assingment.flikrrecentimages.webutils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.assingment.flikrrecentimages.callback.GetURLResponse;
import com.assingment.flikrrecentimages.model.FlikerURLResponse;
import com.assingment.flikrrecentimages.model.Response;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * Created by suyashg on 04/09/16.
 */

public class DefaultClient {

    private String url;
    private GetURLResponse getURLResponse;
    private boolean isImageDownload;
    private static final String EMPTY = "";
    private static final String METHOD_GET = "GET";
    private static final String ERROR_INVALID_URL = "Invalid URL";
    private static final String ERROR_INPUT_OUTPUT = "Error in processing request:";

    public DefaultClient(String url, GetURLResponse getURLResponse, boolean isImageDownload) {
        this.url = url;
        this.getURLResponse = getURLResponse;
        this.isImageDownload = isImageDownload;
    }

    public void executeRequest() {
        Response response = new Response();
        response.setImageResponse(isImageDownload);
        HttpURLConnection httpCon = null;
        InputStream inputStream = null;
        try {
            httpCon = openHttpConnection(url);
            inputStream = httpCon.getInputStream();
        } catch (IOException e) {
            //Error Interface calling
            getURLResponse.errorResposeofGetURL(e, response);
            e.printStackTrace();
        }

        if (isImageDownload) {
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            response.setBitmap(bitmap);
        } else {
            String data = readStream(inputStream);
            Gson gson = new Gson();
            FlikerURLResponse getURLResponse = gson.fromJson(data, FlikerURLResponse.class);
            response.setFlikerURLResponse(getURLResponse);
        }

        try {
            inputStream.close();
        } catch (IOException e) {
            //Error Interface calling
            getURLResponse.errorResposeofGetURL(e, response);
            e.printStackTrace();
        }
        httpCon.disconnect();

        //Success
        getURLResponse.getResponseofGetURL(response);
    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = EMPTY;
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    private HttpURLConnection openHttpConnection(String urlString) throws IOException {
        URL url = new URL(urlString);
        final URLConnection conn = url.openConnection();

        if (!(conn instanceof HttpURLConnection)) {
            throw new IOException(ERROR_INVALID_URL);
        }

        try {
            HttpURLConnection httpCon = (HttpURLConnection) conn;
            httpCon.setAllowUserInteraction(false);
            httpCon.setInstanceFollowRedirects(true);
            httpCon.setRequestMethod(METHOD_GET);
            httpCon.connect();

            int response = httpCon.getResponseCode();
            if (response == HttpURLConnection.HTTP_OK) {
                return httpCon;
            } else {
                throw new IOException(ERROR_INPUT_OUTPUT+response);
            }
        } catch (Exception ex) {
            //ex.printStackTrace();
            throw new IOException(ERROR_INPUT_OUTPUT+ex.getMessage());
        }
    }
}
