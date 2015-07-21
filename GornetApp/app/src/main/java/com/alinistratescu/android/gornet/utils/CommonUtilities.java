package com.alinistratescu.android.gornet.utils;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

import java.math.BigDecimal;

/**
 * Created by Alin on 5/25/2015.
 */
public class CommonUtilities {

    public static boolean isDevelopment() {
        return Constants.logEnvironment == Logger.ENVIRONMENT.DEVELOPMENT;
    }

    /**
     * Services
     */

    public static boolean isServiceRunning(Context context, Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                return true;
            }
        }

        return false;
    }

    public static String convertMToKm(Context context, int m) {

        double d = m / 1000;
        return String.valueOf(CommonUtilities.truncateDoubleToInt(d));
    }

    public static int truncateDoubleToInt(double d) {
        return new BigDecimal(d).setScale(0, BigDecimal.ROUND_DOWN).intValue();
    }

    public static void showAlertDialog(Context context, Class<? extends Activity> activityClass, String title, String message) {
        try {
            if (activityClass.cast(context) != null && !activityClass.cast(context).isFinishing()) {
                new AlertDialog.Builder(context)
                        .setTitle(title)
                        .setMessage(message)
                        .setCancelable(false)
                        .setPositiveButton(context.getString(android.R.string.ok), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
