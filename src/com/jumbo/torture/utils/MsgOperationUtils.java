package com.jumbo.torture.utils;

import com.jumbo.torture.ComposeMsgActivity;
import com.jumbo.torture.R;
import com.jumbo.torture.provider.Torture.TortureMsg;

import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.text.TextUtils;
import android.widget.Toast;


public class MsgOperationUtils{

    private static String TAG = MsgOperationUtils.class.getName();

    public static String handleTitle(Context context,String title,String content){
        if(!TextUtils.isEmpty(title)){
            return title;
        }

        String defaultTitle = context.getString(R.string.empty_title_hint);

        if(!TextUtils.isEmpty(content)){
            int length = content.length();
            defaultTitle = content.substring(0, Math.min(30,length));
            if (length > 30) {
                int lastSpace = title.lastIndexOf(' ');
                if (lastSpace > 0) {
                    title = title.substring(0, lastSpace);
                }
            }
        }

        return defaultTitle;
    }


    public static Uri insert(Context context,String content){
        if(TextUtils.isEmpty(content)){
            return null;
        }

        ContentValues values = new ContentValues();

        values.put(TortureMsg.CREATED_TIME, System.currentTimeMillis());
        values.put(TortureMsg.MODIFY_TIME, System.currentTimeMillis());
        values.put(TortureMsg.TITLE, MsgOperationUtils.handleTitle(context, "", content));
        values.put(TortureMsg.CONTENT,content);

        Uri uri = null;
        try {
            uri = context.getContentResolver().insert(TortureMsg.CONTENT_URI, values);
        } catch (NullPointerException e) {
            LogUtils.e(TAG, e.getMessage());
        }

        return uri;
    }

}