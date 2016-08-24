package com.haozhn.imageselector.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Administrator on 2016/8/13.
 */
public class BitmapUtils {

    private BitmapUtils() {
    }

    /****
     * 将bitmap保存到本地，如果本地已存在该图片，则将该图片删除，再放新的
     * @param fileName
     * @param bitmap
     */
    public static void saveBitmap2Local(String fileName,Bitmap bitmap){
        try {
            File file = new File(fileName);
            if(file.exists()) {
                file.delete();
            }
            try {
                file.createNewFile();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
            try {
                fileOutputStream.flush();
                fileOutputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Bitmap getBitmapFromFile(String filePath, int inSampleSize) {
        FileInputStream f = null;
        try {
            f = new FileInputStream(filePath);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        Bitmap bm = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;//图片的长宽都是原来的1/8
        BufferedInputStream bis = new BufferedInputStream(f);
        bm = BitmapFactory.decodeStream(bis, null, options);
        return bm;
    }

    public static boolean createFile(String filePath)
    {
        try
        {
            File file = new File(filePath);
            if (!file.exists())
            {
                if (!file.getParentFile().exists())
                {
                    file.getParentFile().mkdirs();
                }

                return file.createNewFile();
            }
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return true;
    }


    public static void writeImage(Bitmap bitmap,String destPath,int quality)
    {
        try {
            if (createFile(destPath))
            {
                FileOutputStream out = new FileOutputStream(destPath);
                if (bitmap.compress(Bitmap.CompressFormat.JPEG,quality, out))
                {
                    out.flush();
                    out.close();
                    out = null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
