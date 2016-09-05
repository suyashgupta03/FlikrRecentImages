package com.assingment.flikrrecentimages.ui;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.util.LruCache;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.assingment.flikrrecentimages.R;
import com.assingment.flikrrecentimages.adapters.FlikrRecentPhotosAdapter;
import com.assingment.flikrrecentimages.callback.BitmapCacheCallback;
import com.assingment.flikrrecentimages.callback.GetFlickerRecentPhotos;
import com.assingment.flikrrecentimages.callback.RefreshImageCallback;
import com.assingment.flikrrecentimages.model.FlikerURLResponse;
import com.assingment.flikrrecentimages.model.Photo;
import com.assingment.flikrrecentimages.service.RecentPhotoService;

import java.util.ArrayList;

/**
 * Created by suyashg on 03/09/16.
 */
public class FlikrRecentActivity extends AppCompatActivity implements RefreshImageCallback,
        GetFlickerRecentPhotos, BitmapCacheCallback {

//  Flikr URL
    private static final String FLIKER_RECENT_URL = "https://api.flickr.com/services/rest/?method=flickr.photos.getRecent&api_key=d98f34e2210534e37332a2bb0ab18887&format=json&extras=url_n&nojsoncallback=?";

//  UI Views
    private ProgressBar progressBar;
    private LinearLayout mainBody;
    private ListView listOfImages;

//  WEB Data
    private FlikerURLResponse getURLResponse;

//  Memory cache
    private LruCache<String, Bitmap> mMemoryCache;

//    Adapter for the images
    private FlikrRecentPhotosAdapter photosAdapter;

//  Service layer for recent photo
    private RecentPhotoService recentPhotoService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flikr_recent);
        progressBar = (ProgressBar) findViewById(R.id.main_progress);
        mainBody = (LinearLayout) findViewById(R.id.action_form);
        listOfImages = (ListView) findViewById(R.id.lv_images);

        // Get max available VM memory, exceeding this amount will throw an
        // OutOfMemory exception. Stored in kilobytes as LruCache takes an
        // int in its constructor.
        final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);

        // Use 1/8th of the available memory for this memory cache.
        final int cacheSize = maxMemory / 8;

        mMemoryCache = new LruCache<String, Bitmap>(cacheSize) {
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                // The cache size will be measured in kilobytes rather than
                // number of items.
                return bitmap.getByteCount() / 1024;
            }
        };

        recentPhotoService = new RecentPhotoService();
        photosAdapter = new FlikrRecentPhotosAdapter(this, R.layout.item_recent_photo, new ArrayList<Photo>(), this);
        listOfImages.setAdapter(photosAdapter);

        progressBar.setVisibility(View.VISIBLE);

        recentPhotoService.processRecentRequest(this, FLIKER_RECENT_URL);
    }

    public void addBitmapToMemoryCache(String key, Bitmap bitmap) {
        if (getBitmapFromMemCache(key) == null) {
            mMemoryCache.put(key, bitmap);
        }
    }

    @Override
    public Bitmap getBitmapFromMemCache(String key) {
        return mMemoryCache.get(key);
    }

    @Override
    public void downloadBitmapForView(Photo photo) {
        recentPhotoService.processPhotoRequest(this, photo);
    }

    @Override
    public void refreshTheImage(String url, Bitmap bitmap) {
        System.out.println("refreshTheImage:"+bitmap);
//        Painless threading:Loading ImageView on main thread
//        http://android-developers.blogspot.in/2009/05/painless-threading.html
        addBitmapToMemoryCache(url, bitmap);

        new Thread(new Runnable() {
            public void run() {
                listOfImages.post(new Runnable() {
                    public void run() {
                        photosAdapter.addPhotoToList(recentPhotoService.getProcessedPhotoByUrl(url));
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
    public void getRecentImages(FlikerURLResponse getURLResponse) {
        this.getURLResponse = getURLResponse;
        recentPhotoService.processPhotoRequest(this, getURLResponse.getPhotos().getPhoto());
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
        recentPhotoService.invalidate();
        mMemoryCache.evictAll();
    }
}

