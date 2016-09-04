package com.assingment.flikrrecentimages.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.assingment.flikrrecentimages.R;
import com.assingment.flikrrecentimages.callback.GetFlickerRecentPhotos;
import com.assingment.flikrrecentimages.callback.RefreshImageCallback;
import com.assingment.flikrrecentimages.webutils.AppExecutor;

import com.assingment.flikrrecentimages.model.FlikerURLResponse;

/**
 * Created by suyashg on 03/09/16.
 */
public class FlikrRecentActivity extends AppCompatActivity implements RefreshImageCallback, View.OnClickListener,GetFlickerRecentPhotos {

//  Flikr URL
    private static final String FLIKER_RECENT_URL = "https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=d98f34e2210534e37332a2bb0ab18887&format=json&extras=url_n&nojsoncallback=?";

//  UI Views
    private ProgressBar progressBar;
    private LinearLayout mainBody;
    private ImageView picture;
    private TextView counter;

//  WEB Data
    private FlikerURLResponse getURLResponse;

//  counter for images
    private int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flikr_recent);
        Button mStartExecutor = (Button) findViewById(R.id.start_executor_service);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);
        mainBody = (LinearLayout) findViewById(R.id.action_form);
        picture = (ImageView) findViewById(R.id.imv_image_container);
        counter = (TextView) findViewById(R.id.tv_image_counter);

        mStartExecutor.setOnClickListener(this);
        progressBar.setVisibility(View.VISIBLE);
        picture.setVisibility(View.VISIBLE);

        AppExecutor appExecutor = AppExecutor.getInstance();
        appExecutor.addRecentRequestToExecutor(this, FLIKER_RECENT_URL);
    }

    @Override
    public void refreshTheImage(Bitmap bitmap) {
        System.out.println("refreshTheImage:"+bitmap);
        picture.refreshDrawableState();

//        Painless threading:Loading ImageView on main thread
//        http://android-developers.blogspot.in/2009/05/painless-threading.html
        new Thread(new Runnable() {
            public void run() {
                picture.post(new Runnable() {
                    public void run() {
                        picture.setImageBitmap(bitmap);
                        counter.setText(String.valueOf(++count));
                    }
                });
            }
        }).start();
    }

    @Override
    public void showImageDownloadError(String errorMessage) {
        Toast.makeText(this,"Error:"+errorMessage, Toast.LENGTH_SHORT ).show();
    }

    @Override
    public void onClick(View view) {
        AppExecutor appExecutor = AppExecutor.getInstance();
        appExecutor.addPhotoRequestToExecutor(this, getURLResponse.getPhotos().getPhoto());
    }

    @Override
    public void getRecentImages(FlikerURLResponse getURLResponse) {
        this.getURLResponse = getURLResponse;
        progressBar.setVisibility(View.INVISIBLE);
        mainBody.setVisibility(View.VISIBLE);
        Toast.makeText(this,"Success", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showRecentPhotosError(String errorMessage) {
        Toast.makeText(this,"Error:"+errorMessage, Toast.LENGTH_SHORT ).show();
        progressBar.setVisibility(View.VISIBLE);
        mainBody.setVisibility(View.INVISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AppExecutor appExecutor = AppExecutor.getInstance();
        appExecutor.stop();
    }
}

