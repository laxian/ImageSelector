package com.haozhn.imageselector;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.TextView;

import com.haozhn.imageselector.adapter.BigImageAdapter;
import com.haozhn.imageselector.util.EmptyOnPageChangeListener;
import com.haozhn.imageselector.util.KeyConstant;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by hao on 2016/5/18.
 */
public class BigImageActivity extends BaseActivity{
    private TextView title;
    private ViewPager viewpager;
    private TextView tvDone;
    private BigImageAdapter adapter;

    private ArrayList<String> images;
    private int index;
    @Override
    protected void getBundle(Bundle bundle) {
        super.getBundle(bundle);
        images=bundle.getStringArrayList(KeyConstant.ALLPHOTO);
        index=bundle.getInt(KeyConstant.INDEX,0);
    }

    @Override
    protected void setUpView(View view) {
        $(R.id.left_icon).setVisibility(View.GONE);
        $(R.id.title_bg).setBackgroundColor(ContextCompat.getColor(this,R.color.black));
        $(R.id.div).setVisibility(View.GONE);
        title = $(R.id.title);
        viewpager = $(R.id.viewpager);
        tvDone = $(R.id.tv_done);
        title.setTextColor(ContextCompat.getColor(this,R.color.white));

        viewpager.setAdapter(adapter = new BigImageAdapter(this));
        adapter.updateData(images);
        viewpager.setCurrentItem(index);
        if(images!=null&&images.size()>0){
            title.setText(String.format(Locale.CHINA,"%d/%d", index+1, images.size()));
        }

        viewpager.addOnPageChangeListener(new EmptyOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                title.setText(String.format(Locale.CHINA,"%d/%d", position+1, images.size()));
            }
        });
    }

    @Override
    protected int getInflateLayout() {
        return R.layout.activity_big_image;
    }
}
