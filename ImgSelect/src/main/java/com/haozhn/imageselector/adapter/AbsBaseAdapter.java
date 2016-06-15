package com.haozhn.imageselector.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * edited by yanyi on 16/1/14.
 */
public abstract class AbsBaseAdapter<T> extends BaseAdapter {

    protected List<T> data;

    protected Context context;

    protected LayoutInflater inflater;

    public AbsBaseAdapter(@NonNull Context context) {
        this.data = new ArrayList<T>();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    //加到list后面
    public void addToEnd(List<T> datas) {
        if (datas != null && datas.size() > 0) {
            data.addAll(datas);
            notifyDataSetChanged();
        }
    }

    //加到list前面面
    public void addToFront(List<T> datas) {
        if (datas != null && datas.size() > 0) {
            data.addAll(0, datas);
            notifyDataSetChanged();
        }
    }


    @Override
    public int getCount() {
        if (data == null)
            return 0;
        else
            return data.size();

    }

    @Override
    public T getItem(int position) {
        if (data == null || data.size() == 0||position<0)
            return null;
        else
            return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(@NonNull List<T> data) {
        this.data = data;
    }

    private final Object mLock = new Object();

    public void updateData(@NonNull List<T> data){
        setData(data);
        notifyDataSetChanged();
    }


    public void addAllData(Collection<? extends T> collection) {
        synchronized (mLock) {
            if (collection != null) {
                data.addAll(collection);
            }
        }
        notifyDataSetChanged();
    }

    public void addData(T t){
        if(data!=null){
            data.add(t);
        }else{
            data=new ArrayList<>();
            data.add(t);
        }
        notifyDataSetChanged();
    }
    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }
    public void updateList(List<T> data) {
        this.data = data;
        notifyDataSetChanged();
    }
}
