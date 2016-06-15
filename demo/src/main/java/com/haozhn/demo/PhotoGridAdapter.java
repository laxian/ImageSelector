package com.haozhn.demo;

import android.app.Activity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hao on 2016/3/31.
 */
public class PhotoGridAdapter extends BaseAdapter {
    private List<String> pathList;
    private Activity context;

    public PhotoGridAdapter(Activity context) {
        pathList = new ArrayList<>();
        this.context = context;
    }

    public void setData(List<String> pathList) {
        this.pathList = pathList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return pathList == null ? 1 : pathList.size() + 1;
    }

    @Override
    public Object getItem(int position) {
        return pathList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.grid_item_photo, parent, false);
            viewHolder=new ViewHolder();
            viewHolder.photo= (ImageView) convertView.findViewById(R.id.photo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        if (pathList != null&&pathList.size()>0&& pathList.size() > position) {
            final String path = pathList.get(position);
            if (!TextUtils.isEmpty(path)) {
                Glide.with(context).load(path).into(viewHolder.photo);
            }
        } else {
            Glide.with(context).load(R.drawable.icon_b02_h).into(viewHolder.photo);
        }
        return convertView;
    }

    static class ViewHolder {
        ImageView photo;
    }

}
