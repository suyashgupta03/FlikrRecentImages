package com.assingment.flikrrecentimages.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;

import com.assingment.flikrrecentimages.R;
import com.assingment.flikrrecentimages.callback.BitmapCacheCallback;
import com.assingment.flikrrecentimages.model.Photo;

import java.util.List;

/**
 * Created by suyashg on 05/09/16.
 */

public class FlikrRecentPhotosAdapter extends ArrayAdapter<Photo> {

    private List<Photo> items;
    private BitmapCacheCallback cacheCallback;
    private Context context;
    private int textViewResourceId;

    public FlikrRecentPhotosAdapter(Context context, int textViewResourceId, List<Photo> items, BitmapCacheCallback cacheCallback) {
        super(context, textViewResourceId, items);
        this.items = items;
        this.cacheCallback = cacheCallback;
        this.context = context;
        this.textViewResourceId = textViewResourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(textViewResourceId, null);
        }

        Photo photo = items.get(position);
        ImageView imv = (ImageView) v.findViewById(R.id.imv_recent_photo);
        loadBitmap(photo, imv);
        return v;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    private void loadBitmap(Photo photo, ImageView imageView) {
        final Bitmap bitmap = cacheCallback.getBitmapFromMemCache(photo.getUrl_n());
        if (bitmap != null) {
            imageView.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.mipmap.placeholder);
            cacheCallback.downloadBitmapForView(photo);
        }
    }

    public void addPhotoToList(Photo photo) {
        items.add(photo);
        this.notifyDataSetChanged();
    }
}

