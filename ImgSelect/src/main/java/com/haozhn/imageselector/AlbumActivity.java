package com.haozhn.imageselector;

import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.haozhn.imageselector.adapter.AlbumAdapter;
import com.haozhn.imageselector.model.Album;
import com.haozhn.imageselector.model.Photo;
import com.haozhn.imageselector.model.Result;
import com.haozhn.imageselector.util.FileUtils;
import com.haozhn.imageselector.util.KeyConstant;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hao on 2016/5/10.  相册选择
 */
public class AlbumActivity extends BaseActivity implements AdapterView.OnItemClickListener {
    private static final int SELECT_IMAGE = 1;
    private static final int TAKE_PHOTO = 2;
    private ImageView leftIcon;
    private TextView title;
    private TextView right;
    private ListView albumList;
    private AlbumAdapter adapter;
    public LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {
        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,                  //图片路径
                MediaStore.Images.Media.BUCKET_DISPLAY_NAME,   //相册名字
                MediaStore.Images.Media.DATE_ADDED,            //创建时间
                MediaStore.Images.Media.MIME_TYPE,             //图片类型
                MediaStore.Images.Media.SIZE,                  //图片大小
                MediaStore.Images.Media.BUCKET_ID,             //相册id
                MediaStore.Images.Media._ID                    //图片id
        };

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            return new CursorLoader(context,
                    MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                    IMAGE_PROJECTION[4] + ">0 AND " + IMAGE_PROJECTION[3] + "=? OR " + IMAGE_PROJECTION[3] + "=? ",
                    new String[]{"image/jpeg", "image/png"}, IMAGE_PROJECTION[2] + " DESC");
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data == null || data.getCount() <= 0) return;
            List<Result> results = new ArrayList<>();
            data.moveToFirst();
            do {
                String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                String fileName = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                long id = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[6]));
                long buckId = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[5]));
                Result result = new Result(path, fileName, dateTime, id, buckId);
                results.add(result);
            } while (data.moveToNext());
            processResult(results);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };
    private int limit;
    private ArrayList<Photo> selectPhotos = new ArrayList<>();
    private boolean showCamera;
    private boolean mIsCropSquare;
    private String mLastImg;

    @Override
    protected void getBundle(Bundle bundle) {
        super.getBundle(bundle);
        limit = bundle.getInt(KeyConstant.LIMIT);
        showCamera = bundle.getBoolean(KeyConstant.SHOWCAMERA);
        mIsCropSquare = bundle.getBoolean(KeyConstant.CROP_SQUARE);
    }

    @Override
    protected void setUpView(View view) {
        leftIcon = $(R.id.left_icon);
        title = $(R.id.title);
        albumList = $(R.id.album_list);
        right = $(R.id.right);

        title.setText(R.string.choose_album);
        right.setText(R.string.cancel);
        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setResult(RESULT_CANCELED);
                finish();
            }
        });
        leftIcon.setVisibility(View.GONE);
        adapter = new AlbumAdapter(this);
        adapter.setShowCamera(true);
        albumList.setAdapter(adapter);
        albumList.setOnItemClickListener(this);
        getLoaderManager().restartLoader(0, null, mLoaderCallback);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (showCamera && position == 0) {
            Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmss");
            mLastImg = FileUtils.getSdDownloadFile(this).getPath() + "/" + format.format(new Date()) + ".jpg";
            Uri imageUri = Uri.fromFile(new File(mLastImg));
            //指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换
            intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
            startActivityForResult(intent, TAKE_PHOTO);
        } else {
            Album album = adapter.getItem(--position);
            if (album == null) return;
            Intent intent = new Intent(this, ImageActivity.class);
            intent.putExtra(KeyConstant.LIMIT, limit);
            intent.putExtra(KeyConstant.SELECTPHOTOS, selectPhotos);
            intent.putExtra(KeyConstant.ALBUM, album);
            intent.putExtra(KeyConstant.CROP_SQUARE, mIsCropSquare);
            if (mIsCropSquare) {
                intent.putExtra(KeyConstant.CROP_WIDTH, getIntent().getIntExtra(KeyConstant.CROP_WIDTH, 200));
                intent.putExtra(KeyConstant.CROP_HEIGHT, getIntent().getIntExtra(KeyConstant.CROP_HEIGHT, 200));
                startActivityForResult(intent, KeyConstant.CROP_REQUEST_CODE);
            } else {

                startActivityForResult(intent, SELECT_IMAGE);
            }

        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SELECT_IMAGE) {
            if (resultCode == RESULT_OK) {
                boolean done = data.getBooleanExtra(KeyConstant.ISDONE, false);
                selectPhotos = (ArrayList<Photo>) data.getSerializableExtra(KeyConstant.SELECTPHOTOS);
                if (done) {
                    Intent intent = new Intent();
                    intent.putExtra(KeyConstant.RESULT, selectPhotos);
                    setResult(RESULT_OK, intent);
                    finish();
                }
            } else if (resultCode == RESULT_CANCELED) {
                setResult(RESULT_CANCELED);
                finish();
            }
        } else if (requestCode == TAKE_PHOTO) {
            if (resultCode == RESULT_OK) {
                if (mIsCropSquare) {
                    cropImageFromPhoto(mLastImg);
                    return;
                }


                // 通过这种方式获取的图片，质量太低
//                Bitmap bitmap = data.getParcelableExtra("data");
//                if(bitmap!=null){
//                    String path = getLocalPath();
//                    BitmapUtils.saveBitmap2Local(path, bitmap);
//                    selectPhotos.add(new Photo(1, path, System.currentTimeMillis()));
//

//                }
                if (TextUtils.isEmpty(mLastImg)) {
                    Log.d("result", "终于发现你了======================================");
                }
                //将保存在本地的图片取出并缩小后显示在界面上
                selectPhotos.add(new Photo(1, mLastImg, System.currentTimeMillis()));

                Intent intent = new Intent();
                intent.putExtra(KeyConstant.RESULT, selectPhotos);
                setResult(RESULT_OK, intent);
                finish();
            }
        } else if (requestCode == KeyConstant.CROP_REQUEST_CODE) {
            if (resultCode == RESULT_OK && mIsCropSquare) {
                Intent intent = new Intent();
                String savePath = data.getStringExtra(KeyConstant.CROP_SAVE_PATH);
                intent.putExtra(KeyConstant.CROP_SAVE_PATH, savePath);
                setResult(RESULT_OK, intent);
                finish();
            }
        }
    }

    private void cropImageFromPhoto(String path) {
        Intent cropIntent = new Intent(this, SquareCropActivity.class);
        cropIntent.putExtra(KeyConstant.IMAGE_PATH, path);
        cropIntent.putExtra(KeyConstant.CROP_WIDTH, getIntent().getIntExtra(KeyConstant.CROP_WIDTH, 200));
        cropIntent.putExtra(KeyConstant.CROP_HEIGHT, getIntent().getIntExtra(KeyConstant.CROP_HEIGHT, 200));
        startActivityForResult(cropIntent, KeyConstant.CROP_REQUEST_CODE);
    }

    private String getLocalPath() {
        String cacheDir = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            String sdRoot = Environment.getExternalStorageDirectory().getAbsolutePath();
            String packageName = context.getPackageName();
            cacheDir = sdRoot + File.separator + packageName + File.separator + System.currentTimeMillis() + ".jpg";

            return cacheDir;
        }
        return null;
    }

    private void processResult(List<Result> results) {
        Album all = new Album();
        all.setName(getString(R.string.all_photo));
        List<Album> albumList = new ArrayList<>();
        for (Result result : results) {
            Photo photo = new Photo(result.getId(), result.getPath(), result.getTime());
            Album album = getAlbumById(albumList, result.getBucketId());
            if (album == null) {
                album = new Album();
                album.setId(result.getBucketId());
                album.setName(result.getName());
                albumList.add(album);
            }
            album.getPhotoList().add(photo);
            all.getPhotoList().add(photo);
        }

        if (albumList.size() > 1) {
            albumList.add(0, all);
        }
        adapter.updateData(albumList);
    }

    private Album getAlbumById(List<Album> list, long id) {
        for (Album album : list) {
            if (album.getId() == id) {
                return album;
            }
        }
        return null;
    }

    @Override
    protected int getInflateLayout() {
        return R.layout.activity_album;
    }

    /**
     * 用于保存状态
     */
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putString("last_img_path", mLastImg);
        super.onSaveInstanceState(outState);
    }

    /**
     * 用于恢复状态
     */

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        Log.d("result", "restored");
        super.onRestoreInstanceState(savedInstanceState);
        mLastImg = savedInstanceState.getString("last_img_path");
    }
}
