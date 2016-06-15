package com.haozhn.imageselector.model;

import java.util.ArrayList;

/**
 * Created by hao on 2016/5/12.
 */
public class Album extends BaseObject{
    private long id;
    private String name;
    private ArrayList<Photo> photoList;

    public Album() {
        id=0;
        name=null;
        this.photoList = new ArrayList<>();
    }

    public ArrayList<Photo> getPhotoList() {
        return photoList;
    }

    public void setPhotoList(ArrayList<Photo> photoList) {
        this.photoList = photoList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
}
