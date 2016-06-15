package com.haozhn.imageselector.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.haozhn.imageselector.util.ImageLoader;
import com.haozhn.imageselector.R;
import com.haozhn.imageselector.model.Photo;

import java.util.ArrayList;

/**
 * Created by hao on 2016/4/6.
 */
public class ImageAdapter extends AbsBaseAdapter<Photo> {
    private ArrayList<Photo> pathList;
    private int numLimit;
    private OnCountChangeListener listener;
    public ImageAdapter(@NonNull Context context) {
        super(context);
        pathList = new ArrayList<>();
    }

    public void setPathList(ArrayList<Photo> pathList) {
        this.pathList = pathList;
        notifyDataSetChanged();
    }

    public void setOnCountChangeListener(OnCountChangeListener l){
        this.listener=l;
    }

    public ArrayList<Photo> getPathList() {
        return pathList;
    }

    public void setNumLimit(int numLimit) {
        this.numLimit = numLimit;
    }

    @Override
    public View getView(int position, View convertView, final ViewGroup parent) {
        final ViewHolder viewHolder;
        final Photo object = getItem(position);
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.list_item_image, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.image= (ImageView) convertView.findViewById(R.id.image);
            viewHolder.tvSelect= (TextView) convertView.findViewById(R.id.tv_select);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ImageLoader.loadPhoto(context,object.getPath(),viewHolder.image);
        if (pathList != null) {
            boolean in=false;
            for (Photo p: pathList) {
                if (p.getId()==(object.getId())) {
                    in=true;
                    break;
                }
            }
            if(in){
                viewHolder.tvSelect.setSelected(true);
            }else{
                viewHolder.tvSelect.setSelected(false);
            }
        }
        viewHolder.tvSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewHolder.tvSelect.isSelected()){
                    pathList.remove(object.getPath());
                    viewHolder.tvSelect.setSelected(false);
                }else{
                    if(pathList!=null&&pathList.size()>=numLimit){
                        Toast.makeText(context, R.string.image_out_of_range, Toast.LENGTH_LONG).show();
                        viewHolder.tvSelect.setSelected(false);
                    }else{
                        if (pathList == null)
                            pathList = new ArrayList<>();
                        pathList.add(object);
                        viewHolder.tvSelect.setSelected(true);
                    }
                }
                listener.OnCountChange(pathList.size());
            }
        });
        return convertView;
    }

    static class ViewHolder {
        ImageView image;
        TextView tvSelect;
    }

    public interface OnCountChangeListener{
        void OnCountChange(int count);
    }
}
