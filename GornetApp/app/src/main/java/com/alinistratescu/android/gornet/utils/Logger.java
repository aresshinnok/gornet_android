package com.alinistratescu.android.gornet.utils;

import android.util.Log;

/**
 * Created by Sergiu on 03/02/15.
 */
public class Logger {

    private static String TAG = Constants.TAG;


    public static void d(String string) {
        if (isDevelopment()) {
            if (string != null) {
                Log.d(TAG, string);
            }
        }
    }

    private static boolean isDevelopment() {
        return CommonUtilities.isDevelopment();
    }

    public static enum ENVIRONMENT {
        DEVELOPMENT, PRODUCTION
    }

}
