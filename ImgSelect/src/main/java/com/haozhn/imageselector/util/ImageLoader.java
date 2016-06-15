package com.haozhn.imageselector.util;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.haozhn.imageselector.R;

/**
 * Created by hao on 2016/5/12.
 */
public class ImageLoader {
    public static void loadPhoto(Context context, String data, ImageView imageView){
        Glide.with(context).load(data).centerCrop().error(R.drawable.default_error).into(imageView);
    }

    public static void loadPhoto(Context context, int data, ImageView imageView){
        Glide.with(context).load(data).centerCrop().error(R.drawable.default_error).into(imageView);
    }

    public static void loadPhotoNoType(Context context, int data, ImageView imageView){
        Glide.with(context).load(data).error(R.drawable.default_error).into(imageView);
    }

    public static void loadPhotoNoType(Context context, String data, ImageView imageView){
        Glide.with(context).load(data).error(R.drawable.default_error).into(imageView);
    }
}
