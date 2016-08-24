package com.haozhn.imageselector;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import com.haozhn.imageselector.util.BitmapUtils;
import com.haozhn.imageselector.util.KeyConstant;
import com.haozhn.imageselector.widget.CropImageView;

import java.io.File;

public class SquareCropActivity extends Activity {
    private static String sImgRoot;

    static {
        sImgRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    private CropImageView mCropImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_square_crop);
        initViewAndData();
    }


    private void initViewAndData() {
        findViewById(R.id.tv_crop_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
            }
        });
        findViewById(R.id.tv_crop_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SquareCropActivity.this.finish();
            }
        });
        mCropImg = (CropImageView) findViewById(R.id.crop_square);
        String path = getIntent().getStringExtra(KeyConstant.IMAGE_PATH);
        Log.i("xxx", "onCreate: " + path);
        Bitmap bmp = BitmapFactory.decodeFile(path);
        Drawable drawable = new BitmapDrawable(bmp);
        int cropWidth = getIntent().getIntExtra(KeyConstant.CROP_WIDTH,200);
        int cropHeight = getIntent().getIntExtra(KeyConstant.CROP_HEIGHT,200);
        mCropImg.setDrawable(drawable, cropWidth, cropHeight);
    }

    private void save() {
        String path = sImgRoot + File.separator + System.currentTimeMillis() + ".png";
        BitmapUtils.writeImage(mCropImg.getCropImage(), path, 100);
        Intent intent = new Intent();
        intent.putExtra(KeyConstant.CROP_SAVE_PATH, path);
        setResult(RESULT_OK, intent);
        finish();
        finish();
    }
}
