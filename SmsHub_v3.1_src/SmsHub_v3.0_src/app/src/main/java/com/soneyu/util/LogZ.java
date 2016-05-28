package com.soneyu.util;

/**
 * Created by songnob on 15/08/2015.
 */

import android.util.Log;

public class LogZ
{
    private static String TAG = "LOGZ";
    private static final boolean SHOW_LOG = true;

    public static void setTag(String tag)
    {
        TAG = tag;
    }
    public static void i(String msg, Object... objs)
    {
        if (SHOW_LOG)
        {
            Log.i(TAG, String.format(msg, objs));
        }
    }
    public static void i(Object tag, String msg,  Object... objs)
    {
        if (SHOW_LOG)
        {
            Log.i(tag.getClass().getName(), String.format(msg, objs));
        }
    }
    public static void e(String msg, Exception ex, Object... objs)
    {
        if (SHOW_LOG)
        {
            Log.e(TAG, String.format(msg, objs));
            ex.printStackTrace();
            Log.i(TAG, "======================== END OF STACK TRACE =============================\n\n\n");
        }
    }

    public static void d(String msg, Object... objs)
    {
        if (SHOW_LOG)
        {
            Log.d(TAG, String.format(msg, objs));
        }
    }
}