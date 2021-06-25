package com.lookballs.sqlite.utils;

import android.util.Log;

public class LogUtils {

    private static final String LOG_TAG = "QuickSqlite";
    private static boolean isLogEnabled = false;

    public static void setLogEnabled(boolean logEnabled) {
        isLogEnabled = logEnabled;
    }

    public static boolean getLogEnabled() {
        return isLogEnabled;
    }

    public static void i(String msg) {
        if (getLogEnabled()) {
            Log.i(LOG_TAG, msg);
        }
    }

    public static void e(String msg) {
        if (getLogEnabled()) {
            Log.e(LOG_TAG, msg);
        }
    }
}
