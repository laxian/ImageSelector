package com.haozhn.imageselector;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.haozhn.imageselector.adapter.ImageAdapter;
import com.haozhn.imageselector.model.Album;
import com.haozhn.imageselector.model.Photo;
import com.haozhn.imageselector.util.KeyConstant;

import java.util.ArrayList;

/**
 * Created by hao on 2016/5/13.
 */
public class ImageActivity extends BaseActivity implements View.OnClickListener,
        ImageAdapter.OnCountChangeListener,AdapterView.OnItemClickListener{
    private static final int PREVIEW = 1;
    private ImageView leftIcon;
    private TextView title;
    private TextView right;
    private TextView tvPreview;
    private TextView tvDone;
    GridView galleryList;
    private int limit;
    private ArrayList<Photo> selectPhotos;
    private Album album;

    private ImageAdapter adapter;
    @Override
    protected void getBundle(Bundle bundle) {
        super.getBundle(bundle);
        limit = bundle.getInt(KeyConstant.LIMIT, 0);
        selectPhotos = (ArrayList<Photo>) bundle.getSerializable(KeyConstant.SELECTPHOTOS);
        album = (Album) bundle.getSerializable(KeyConstant.ALBUM);
        if(album==null) album=new Album();
        if(selectPhotos==null) selectPhotos=new ArrayList<>();
    }

    @Override
    protected void setUpView(View view) {
        leftIcon=$(R.id.left_icon);
        title=$(R.id.title);
        tvPreview=$(R.id.tv_preview);
        tvDone=$(R.id.tv_done);
        galleryList=$(R.id.gallery_list);
        right=$(R.id.right);

        right.setText(R.string.cancel);
        right.setOnClickListener(this);
        leftIcon.setOnClickListener(this);
        tvDone.setOnClickListener(this);
        tvPreview.setOnClickListener(this);
        title.setText(album.getName());
        changeTvByCount(selectPhotos.size());
        galleryList.setAdapter(adapter=new ImageAdapter(this));
        galleryList.setOnItemClickListener(this);
        adapter.setNumLimit(limit);
        adapter.setPathList(selectPhotos);
        adapter.setData(album.getPhotoList());
        adapter.setOnCountChangeListener(this);
    }

    @Override
    protected int getInflateLayout() {
        return R.layout.activity_image;
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.left_icon){
            back(false);
        }else if(id==R.id.right){
            setResult(RESULT_CANCELED);
            finish();
        }else if(id==R.id.tv_done){
            back(true);
        }else if(id==R.id.tv_preview){
            if(selectPhotos==null||selectPhotos.size()==0){
                showToast(R.string.you_not_select_image);
                return;
            }
            Intent intent=new Intent(this,PreviewActivity.class);
            intent.putExtra(KeyConstant.ALLPHOTO,selectPhotos);
            intent.putExtra(KeyConstant.INDEX,0);
            intent.putExtra(KeyConstant.SELECTPHOTOS,selectPhotos);
            startActivityForResult(intent, PREVIEW);
        }
    }

    private void back(boolean done){
        Intent intent=new Intent();
        intent.putExtra(KeyConstant.SELECTPHOTOS,adapter.getPathList());
        intent.putExtra(KeyConstant.ISDONE,done);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        back(false);
    }

    @Override
    public void OnCountChange(int count) {
        changeTvByCount(count);
    }

    private void changeTvByCount(int count){
        if(count>0){
            if(count<100){
                tvDone.setText(String.format(getString(R.string.done_count), count));
            }else{
                tvDone.setText(R.string.done_99);
            }
            tvDone.setEnabled(true);
        }else{
            tvDone.setText(R.string.done);
            tvDone.setEnabled(false);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(this,PreviewActivity.class);
        intent.putExtra(KeyConstant.ALLPHOTO,album.getPhotoList());
        intent.putExtra(KeyConstant.INDEX,position);
        intent.putExtra(KeyConstant.SELECTPHOTOS,adapter.getPathList());
        intent.putExtra(KeyConstant.LIMIT, limit);
        startActivityForResult(intent, PREVIEW);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==PREVIEW&&resultCode==RESULT_OK){
            boolean done=data.getBooleanExtra(KeyConstant.ISDONE,false);
            selectPhotos= (ArrayList<Photo>) data.getSerializableExtra(KeyConstant.SELECTPHOTOS);
            adapter.setPathList(selectPhotos);
            changeTvByCount(selectPhotos.size());
            if(done){
                back(true);
            }
        }
    }
}
