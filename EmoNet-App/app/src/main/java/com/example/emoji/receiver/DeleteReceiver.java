package com.example.emoji.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.BaseAdapter;

import com.example.emoji.model.Picture;
import java.util.List;

public class DeleteReceiver extends BroadcastReceiver {
    private List<Picture> pictures;
    private BaseAdapter adapter;

    public DeleteReceiver(List<Picture> pictures, BaseAdapter adapter) {
        this.pictures = pictures;
        this.adapter = adapter;
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        int pictureId = intent.getIntExtra("pictureId", -1);
        if (pictureId != -1) {
            for (int i = 0; i < pictures.size(); i++) {
                if (pictures.get(i).getId() == pictureId) {
                    pictures.remove(i);
                    adapter.notifyDataSetChanged();
                    break;
                }
            }
        }
    }
}