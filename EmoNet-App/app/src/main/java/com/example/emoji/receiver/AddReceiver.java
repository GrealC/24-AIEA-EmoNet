package com.example.emoji.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.BaseAdapter;

import com.example.emoji.adapter.PictureAdapter;
import com.example.emoji.dao.PictureDao;
import com.example.emoji.model.Picture;

import java.util.List;

public class AddReceiver extends BroadcastReceiver {


    private int uid;
    private PictureAdapter adapter;
    private PictureDao pictureDao;
    public static final String ACTION_ADD_COMPLETED = "com.example.emoji.ACTION_ADD_COMPLETED";
    public AddReceiver(int uid, PictureAdapter adapter) {
        this.uid = uid;
        this.adapter = adapter;
    }
    @Override
    public void onReceive(Context context, Intent intent) {
        pictureDao = new PictureDao(context);

        List<Picture> pictures = pictureDao.getPicturesByUid(uid);
        adapter.updatePictures(pictures);
        adapter.notifyDataSetChanged();
    }
}
