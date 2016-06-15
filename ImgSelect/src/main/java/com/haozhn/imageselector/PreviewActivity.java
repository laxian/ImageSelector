package com.haozhn.imageselector;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.haozhn.imageselector.adapter.PreviewImageAdapter;
import com.haozhn.imageselector.model.Photo;
import com.haozhn.imageselector.util.EmptyOnPageChangeListener;
import com.haozhn.imageselector.util.KeyConstant;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by hao on 2016/4/6.
 */
public class PreviewActivity extends BaseActivity implements View.OnClickListener {

    private ImageView leftIcon;
    private TextView title;
    private ImageView rightIcon;
    private RelativeLayout titleBg;
    private ViewPager viewpager;
    private TextView tvDone;

    private ArrayList<Photo> selectPhotos;
    private ArrayList<Photo> allPhotos;
    private int limit;
    private PreviewImageAdapter adapter;
    private int index;

    @Override
    protected void getBundle(Bundle bundle) {
        super.getBundle(bundle);
        selectPhotos = (ArrayList<Photo>) bundle.getSerializable(KeyConstant.SELECTPHOTOS);
        allPhotos = (ArrayList<Photo>) bundle.getSerializable(KeyConstant.ALLPHOTO);
        index = bundle.getInt(KeyConstant.INDEX, 0);
        limit = bundle.getInt(KeyConstant.LIMIT, Integer.MAX_VALUE);
        if (selectPhotos == null) selectPhotos = new ArrayList<>();
        if (allPhotos == null) allPhotos = new ArrayList<>();
    }

    @Override
    protected void setUpView(View view) {
        leftIcon = $(R.id.left_icon);
        title = $(R.id.title);
        rightIcon = $(R.id.right_icon);
        titleBg = $(R.id.title_bg);
        viewpager = $(R.id.viewpager);
        tvDone = $(R.id.tv_done);
        $(R.id.div).setVisibility(View.GONE);

        title.setTextColor(ContextCompat.getColor(this,R.color.white));
        titleBg.setBackgroundColor(ContextCompat.getColor(this, R.color.black));
        initTitleByAlbum(allPhotos, index);
        leftIcon.setBackgroundResource(R.drawable.back1);
        leftIcon.setOnClickListener(this);
        rightIcon.setOnClickListener(this);
        tvDone.setOnClickListener(this);
        changeTvByCount(selectPhotos.size());

        viewpager.setAdapter(adapter = new PreviewImageAdapter(this));
        adapter.updateData(allPhotos);
        viewpager.setCurrentItem(index);
        updateCheck(allPhotos,index);

        viewpager.addOnPageChangeListener(new EmptyOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                initTitleByAlbum(allPhotos, position);
                updateCheck(allPhotos,position);
            }
        });
    }

    private void updateCheck(ArrayList<Photo> all, int position) {
        Photo photo=all.get(position);
        if(selectPhotos.contains(photo)){
            rightIcon.setSelected(true);
        }else{
            rightIcon.setSelected(false);
        }
    }

    private void initTitleByAlbum(ArrayList<Photo> all, int index) {
        if (all != null && all.size() > 0) {
            rightIcon.setVisibility(View.VISIBLE);
            title.setVisibility(View.VISIBLE);
            title.setText(String.format(Locale.CHINA, "%d/%d", index + 1, all.size()));
        } else {
            rightIcon.setVisibility(View.GONE);
            title.setVisibility(View.GONE);
        }

    }

    @Override
    protected int getInflateLayout() {
        return R.layout.activity_preview;
    }

    private void back(boolean done) {
        Intent intent = new Intent();
        intent.putExtra(KeyConstant.SELECTPHOTOS, selectPhotos);
        intent.putExtra(KeyConstant.ISDONE,done);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        int id=v.getId();
        if(id==R.id.left_icon){
            back(false);
        }else if(id==R.id.right_icon){
            if (rightIcon.isSelected()) {
                Photo photo = allPhotos.get(viewpager.getCurrentItem());
                selectPhotos.remove(photo);
                rightIcon.setSelected(false);
            } else {
                if(selectPhotos.size()>=limit){
                    showToast(R.string.image_out_of_range);
                    return;
                }
                Photo photo = allPhotos.get(viewpager.getCurrentItem());
                selectPhotos.add(photo);
                rightIcon.setSelected(true);
            }
            updateCheck(allPhotos,viewpager.getCurrentItem());
            changeTvByCount(selectPhotos.size());
        }else if(id==R.id.tv_done){
            back(true);
        }
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
    public void onBackPressed() {
        back(false);
    }
}
