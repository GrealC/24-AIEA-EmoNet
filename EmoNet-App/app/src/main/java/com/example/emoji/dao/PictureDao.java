package com.example.emoji.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import com.example.emoji.database.PictureDatabaseHelper;
import com.example.emoji.model.Picture;

import java.util.ArrayList;
import java.util.List;

/*
*  图片数据处理类：存储在Sqlite数据库中
*  1. 插入图片
*  2. 获取图片
*  3. 删除图片
*
* */
public class PictureDao {

    private SQLiteDatabase db;
    private PictureDatabaseHelper dbHelper;

    public PictureDao(Context context) {
        dbHelper = new PictureDatabaseHelper(context);
        db = dbHelper.getWritableDatabase();
    }

    public void insertPicture(Picture picture) {

        ContentValues values = new ContentValues();
        values.put("uid", picture.getUid());
        values.put("path", picture.getPath());
        values.put("name", picture.getName());

        db.insert("pictures", null, values);
    }

    public List<Picture> getPicturesByUid(int uid) {

        List<Picture> pictures = new ArrayList<>();
        Cursor cursor = db.query("pictures", null, "uid=?", new String[]{String.valueOf(uid)}, null, null, null);

        if (cursor != null) {
            while (cursor.moveToNext()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                String path = cursor.getString(cursor.getColumnIndexOrThrow("path"));
                String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
                pictures.add(new Picture(id, uid, path, name));
            }
            cursor.close();
        }
        return pictures;
    }

    public Picture getPictureById(int id) {

        Cursor cursor = db.query("pictures", null, "id=?", new String[]{String.valueOf(id)}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int uid = cursor.getInt(cursor.getColumnIndexOrThrow("uid"));
            String path = cursor.getString(cursor.getColumnIndexOrThrow("path"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            cursor.close();
            return new Picture(id, uid, path, name);
        }
        return null;
    }

    public void deletePicture(int id) {
        db.delete("pictures", "id=?", new String[]{String.valueOf(id)});
    }
    public void close() {
        db.close();
    }
}
