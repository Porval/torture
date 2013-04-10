package com.jumbo.torture.utils;

import com.jumbo.torture.R;

import android.content.Context;
import android.text.TextUtils;


public class MsgOperationUtils{

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


}