package com.haozhn.imageselector.model;

/**
 * Created by hao on 2016/5/12.
 */
public class Result {
    private String path;
    private String name;
    private long time;
    private long id;
    private long bucketId;

    public Result(String path, String name, long time, long id, long bucketId) {
        this.path = path;
        this.name = name;
        this.time = time;
        this.id = id;
        this.bucketId = bucketId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getBucketId() {
        return bucketId;
    }

    public void setBucketId(long bucketId) {
        this.bucketId = bucketId;
    }
}
