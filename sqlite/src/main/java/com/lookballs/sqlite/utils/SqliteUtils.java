package com.lookballs.sqlite.utils;

import android.os.Handler;
import android.os.Looper;

import java.util.Arrays;

/**
 * 工具类
 */
public class SqliteUtils {

    private static final Handler HANDLER = new Handler(Looper.getMainLooper());

    /**
     * 只有满足条件才在主线程中执行
     */
    public static boolean runOnUiThread(boolean execute, Runnable r) {
        if (execute) {
            return runOnUiThread(r);
        }
        return false;
    }

    /**
     * 在主线程中执行
     */
    public static boolean runOnUiThread(Runnable r) {
        return HANDLER.post(r);
    }

    /**
     * 只有满足条件才在延迟一段时间执行
     */
    public static boolean postDelayed(boolean execute, Runnable r, long delayMillis) {
        if (execute) {
            return postDelayed(r, delayMillis);
        }
        return false;
    }

    /**
     * 延迟一段时间执行
     */
    public static boolean postDelayed(Runnable r, long delayMillis) {
        return HANDLER.postDelayed(r, delayMillis);
    }

    /**
     * 合并多个数组
     */
    public static <T> T[] concatAll(T[] first, T[]... rest) {
        if (first == null) {
            return null;
        }
        int totalLength = first.length;
        for (T[] array : rest) {
            if (array != null) {
                totalLength += array.length;
            }
        }
        T[] result = Arrays.copyOf(first, totalLength);
        int offset = first.length;
        for (T[] array : rest) {
            if (array != null) {
                System.arraycopy(array, 0, result, offset, array.length);
                offset += array.length;
            }
        }
        return result;
    }
}
