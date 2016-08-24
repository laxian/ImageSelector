package com.haozhn.demo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import com.haozhn.imageselector.util.ImageSelect;

import java.util.ArrayList;

public class MainActivity extends Activity implements AdapterView.OnItemClickListener {
    private static final int ALBUM = 1;
    private ArrayList<String> pathList = new ArrayList<>();
    private GridView gvPhoto;
    private PhotoGridAdapter gridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        gvPhoto = (GridView) findViewById(R.id.gv_photo);
        gvPhoto.setAdapter(gridAdapter = new PhotoGridAdapter(this));
        gvPhoto.setOnItemClickListener(this);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (gridAdapter.getCount() - 1 == position) {
            startActivityForResult(new ImageSelect.Builder(this)
                    .setLimit(9)
                    .setMode(ImageSelect.MODE_SELECT)
                    .build(), ALBUM);
        } else {
            startActivity(new ImageSelect.Builder(this)
                    .setMode(ImageSelect.MODE_PREVIEW)
                    .setPreSource(pathList)
                    .setIndex(position)
                    .build());
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ALBUM && resultCode == RESULT_OK) {
            pathList = ImageSelect.getImagePath(data);
            gridAdapter.setData(pathList);
        }
    }
}

