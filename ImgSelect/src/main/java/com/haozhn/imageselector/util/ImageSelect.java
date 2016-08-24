package com.haozhn.imageselector.util;

import android.app.Activity;
import android.content.Intent;

import com.haozhn.imageselector.AlbumActivity;
import com.haozhn.imageselector.BigImageActivity;
import com.haozhn.imageselector.model.Photo;

import java.util.ArrayList;

/**
 * Created by hao on 2016/5/12.
 */
public class ImageSelect {

    public static final int MODE_SELECT=1;
    public static final int MODE_PREVIEW=2;

    public static ArrayList<String> getImagePath(Intent data){
        ArrayList<String> result=new ArrayList<>();
        ArrayList<Photo> list= (ArrayList<Photo>) data.getSerializableExtra(KeyConstant.RESULT);
        for (Photo photo : list) {
            result.add(photo.getPath());
        }
        return result;
    }

    public static final class Builder{
        private int mode;
        private int limit;
        private int index;
        private ArrayList<String> imgs;
        private Activity activity;
        private boolean bShowCamera;

        public Builder(Activity activity){
            this.activity=activity;
        }
        public  Builder setMode(int mode){
            this.mode=mode;
            return this;
        }

        public Builder setLimit(int limit){
            this.limit=limit;
            return this;
        }

        public Builder setShowCamera(boolean bval){
            this.bShowCamera = bval;
            return this;
        }

        public Builder setPreSource(ArrayList<String> list){
            imgs=list;
            return this;
        }

        public Builder setIndex(int index){
            this.index=index;
            return this;
        }

        public Intent build(){
            Intent intent=new Intent();
            switch (mode){
                case MODE_SELECT:
                    intent.setClass(activity,AlbumActivity.class);
                    intent.putExtra(KeyConstant.LIMIT,limit);
                    intent.putExtra(KeyConstant.SHOWCAMERA,bShowCamera);
                    break;
                case MODE_PREVIEW:
                    intent.setClass(activity,BigImageActivity.class);
                    intent.putStringArrayListExtra(KeyConstant.ALLPHOTO,imgs);
                    intent.putExtra(KeyConstant.INDEX,index);
                    break;
            }
            return intent;
        }
    }
}
