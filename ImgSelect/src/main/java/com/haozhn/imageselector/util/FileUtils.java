package com.haozhn.imageselector.util;

import android.content.Context;
import android.os.Environment;

import java.io.File;

/**
 * Created by zwx on 16-8-16.
 */
public class FileUtils {
    private FileUtils(){}


    public static File getSdDownloadFile(Context context){
        return context.getExternalFilesDir(Environment.DIRECTORY_PICTURES);
    }
}
