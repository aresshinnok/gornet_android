package com.alinistratescu.android.gornet;

import android.app.Application;
import android.util.Log;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.Logger;
import com.google.android.gms.analytics.Tracker;

import java.io.File;
import java.util.HashMap;

/**
 * Created by Alin on 5/25/2015.
 */
public class GornetApplication extends Application {
    private static GornetApplication instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
    }

    public static GornetApplication getInstance(){
        return instance;
    }

    public enum TrackerName {
        APP_TRACKER
    }

    private HashMap<TrackerName, Tracker> mTrackers = new HashMap<TrackerName, Tracker>();


    /**
     * Setup the Google analytics tracker
     * @param trackerId enum
     * @return Tracker
     * @param trackerId  is a string like UA-000000-01. It must be included in your tracking code to tell Google Analytics which account and property to send data to
     */
    public synchronized Tracker getTracker(TrackerName trackerId) {
        if (!mTrackers.containsKey(trackerId)) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            analytics.setDryRun(false);
            analytics.getLogger().setLogLevel(Logger.LogLevel.INFO);
            analytics.setLocalDispatchPeriod(5);
            Tracker t = analytics.newTracker(R.xml.analytics_app_tracker);
            t.enableAdvertisingIdCollection(true);
            mTrackers.put(trackerId, t);
        }
        return mTrackers.get(trackerId);
    }

    public void clearApplicationData() {
        File cache = getCacheDir();
        File appDir = new File(cache.getParent());
        if(appDir.exists()){
            String[] children = appDir.list();
            for(String s : children){
                if(!s.equals("lib")){
                    deleteDir(new File(appDir, s));
                    Log.i("TAG", "File /data/data/APP_PACKAGE/" + s + " DELETED");
                }
            }
        }
    }

    public static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        return dir.delete();
    }
}
