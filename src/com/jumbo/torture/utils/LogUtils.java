package com.jumbo.torture.utils;


import android.content.Context;
import android.util.Log;

public class LogUtils {

    public static String LOG_MODE_DEBUG = "debug";
    public static String LOG_MODE_TEST	= "test";
    public static String LOG_MODE_PROD	= "prod";

    private static String LOG_FILTER = "LogUtil";
    private static String mMode = LOG_MODE_DEBUG;

    public static void init(Context context,String mode){
        LOG_FILTER = LOG_FILTER + "_" + context.getPackageName();

        Log.i(LOG_FILTER," set LogUtils mode " + mode);

        mMode = mode;
    }

    public static void e(String tag,String content){
        String log = tag + " " + content;
        if(print()){
            Log.e(LOG_FILTER, log);
        }
    }

    public static void d(String tag,String content){
        String log = tag + " " + content;
        if(print()){
            Log.d(LOG_FILTER, log);
        }
    }

    public static void i(String tag,String content){
        String log = tag + " " + content;
        if(print()){
            Log.i(LOG_FILTER, log);
        }
    }

    public static void w(String tag,String content){
        String log = tag + " " + content;
        if(print()){
            Log.w(LOG_FILTER, log);
        }
    }

    public static void e(String tag,Exception e){
        Log.e(LOG_FILTER,tag + " exception track --------------------------------");
        e.printStackTrace();
    }

    private static boolean print(){
        if(LOG_MODE_PROD.equals(mMode)){
            return false;
        }
        return true;
    }

}