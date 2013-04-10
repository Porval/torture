package com.jumbo.torture.provider;


import com.jumbo.torture.provider.Torture.TortureMsg;
import com.jumbo.torture.utils.LogUtils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String TAG = DataBaseHelper.class.getName();

    private static final int DATABASE_VERSION 	= 1;
    private static final String DATABASE_NAME 	= "torture.db";

    public static final String MSG_TABLE_NAME	="tmsg";

    public DataBaseHelper(Context context) {
        super(context,DATABASE_NAME, null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + MSG_TABLE_NAME + " ("
            + TortureMsg._ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
            + TortureMsg.TITLE + " TEXT,"
            + TortureMsg.CONTENT + " TEXT,"
            + TortureMsg.COMMENTS + " TEXT,"
            + TortureMsg.CREATED_TIME + " LONG,"
            + TortureMsg.MODIFY_TIME + " LONG,"
            + TortureMsg.PRIORITY + " INTERGER,"
            + TortureMsg.STATUS + " INTERGER,"
            + TortureMsg.TAGS_ID + " TEXT"
            + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
         LogUtils.w(TAG, "Upgrading database from version " + oldVersion + " to "
                 + newVersion + ", which will destroy all old data");
         db.execSQL("DROP TABLE IF EXISTS " + MSG_TABLE_NAME);
         onCreate(db);
    }



}