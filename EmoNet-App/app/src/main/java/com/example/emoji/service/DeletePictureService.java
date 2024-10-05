package com.example.emoji.service;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.example.emoji.dao.PictureDao;
import com.example.emoji.model.Picture;

import java.io.File;

/*
*  服务：后台删除图片
* */
public class DeletePictureService extends IntentService {

    public static final String ACTION_DELETE_COMPLETED = "com.example.emoji.ACTION_DELETE_COMPLETED";
    private static final int NOTIFICATION_ID = 1;
    private static final String NOTIFICATION_CHANNEL_ID = "delete_picture_channel";


    public DeletePictureService() {
        super("DeletePictureService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null) {
            int pictureId = intent.getIntExtra("pictureId", -1);

            if (pictureId != -1) {
                PictureDao pictureDAO = new PictureDao(this);
                Picture picture = pictureDAO.getPictureById(pictureId);

                if (picture != null) {
                    // 删除本地文件
                    File file = new File(picture.getPath());
                    if (file.exists()) {
                        file.delete();
                    }

                    // 删除数据库记录
                    pictureDAO.deletePicture(pictureId);

                    // 发送删除完成广播
                    Intent deleteCompletedIntent = new Intent(ACTION_DELETE_COMPLETED);
                    deleteCompletedIntent.putExtra("pictureId", pictureId);
                    sendBroadcast(deleteCompletedIntent);

                    showNotification("图片已删除", "ID为 " + pictureId + " 的图片已成功删除。");
                }

                pictureDAO.close();
            }
        }
    }

    private void showNotification(String title, String content) {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    NOTIFICATION_CHANNEL_ID,
                    "Delete Picture Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            notificationManager.createNotificationChannel(channel);
        }

        Notification notification = new Notification.Builder(this, NOTIFICATION_CHANNEL_ID)
                .setContentTitle(title)
                .setContentText(content)
                .setSmallIcon(android.R.drawable.ic_delete)
                .build();

        notificationManager.notify(NOTIFICATION_ID, notification);
    }
}
