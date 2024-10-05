package com.example.emoji.model;

public class Picture {
    int id;
    int uid;
    String path;
    String name;


    public Picture(){}
    public Picture(int uid, String path, String name)
    {
        this.uid = uid;
        this.path = path;
        this.name = name;

    }

    public Picture(int id, int uid, String path, String name) {
        this.id = id;
        this.uid = uid;
        this.path = path;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
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
}
