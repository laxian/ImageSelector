package com.haozhn.imageselector.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.haozhn.imageselector.R;
import com.haozhn.imageselector.util.BitmapUtils;
import com.haozhn.imageselector.util.ImageLoader;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;

import uk.co.senab.photoview.PhotoView;

/**
 * Created by hao on 2016/4/6.
 */
public class BigImageAdapter extends PagerAdapter {
    private List<View> views;
    private Context context;
    private ArrayList<String> photos;

    public BigImageAdapter(Context context) {
        this.context = context;
        views = new ArrayList<>();
        photos=new ArrayList<>();
    }

    public void updateData(ArrayList<String> photos){
        this.photos=photos;
        views=new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }


    @Override
    public int getCount() {
        return photos.size();
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        View view = View.inflate(context, R.layout.viewpage_image, null);
        PhotoView image = (PhotoView) view.findViewById(R.id.image);
        if(photos!=null&&photos.get(position)!=null){
//            ImageLoader.loadPhotoNoType(context,photos.get(position),image);
//            image.setImageURI(Uri.parse(photos.get(position)));
            Bitmap bm = BitmapUtils.getBitmapFromFile(photos.get(position), 4);
            image.setImageBitmap(bm);
        }else{
            ImageLoader.loadPhotoNoType(context,R.drawable.default_error,image);
        }

        views.add(view);
        container.addView(view);
        return view;
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == arg1;
    }
}
