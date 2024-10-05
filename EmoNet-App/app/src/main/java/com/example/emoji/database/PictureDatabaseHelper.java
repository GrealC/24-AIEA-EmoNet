package com.example.emoji.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
*      sqlite数据库操作
* */
public class PictureDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "pictures.db";
    private static final int DATABASE_VERSION = 1;

    public PictureDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_PICTURES_TABLE = "CREATE TABLE pictures ("
                + "id INTEGER PRIMARY KEY AUTOINCREMENT,"
                + "uid INTEGER,"
                + "path TEXT,"
                + "name TEXT"
                + ")";
        db.execSQL(CREATE_PICTURES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS pictures");
        onCreate(db);
    }
}
