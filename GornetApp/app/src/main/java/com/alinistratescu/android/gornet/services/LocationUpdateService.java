package com.alinistratescu.android.gornet.services;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;

import com.alinistratescu.android.gornet.activitydelegates.location.LocationDelegate;
import com.alinistratescu.android.gornet.activitydelegates.location.callbacks.LocationChangedCallback;
import com.alinistratescu.android.gornet.eventbus.BusProvider;
import com.alinistratescu.android.gornet.eventbus.LocationUpdatedEvent;

/**
 * Created by Catalin Matusa on 15.04.2015.
 */

/**
 * Location Update Service </br>
 * A service which updates the current location in background. It can only be stopped by the system or by calling stopService() </br>
 * There are two main lifecycle methods for a Service: onCreate() and onDestroy()
 */
public class LocationUpdateService extends Service implements LocationChangedCallback {


    /**
     * The class object of the activity that delegates the location management
     */
    private static Class<? extends Activity> activityClass;

    /**
     * The location delegate used to get current locations
      */
    private static LocationDelegate locationDelegate;

    /**
     * Default interval time for updating the location
     */
    private static long updateStatusInterval = 15000L;

    @Override
    public void onLocationChanged(Location newLocation) {
        BusProvider.getInstance().getBus().post(new LocationUpdatedEvent(newLocation));
    }



    public static void startServiceWithLoopTime(Context context, Class<? extends Activity> activityClass, Intent intentService, long updateStatusInterval) {
        context.startService(intentService);
        LocationUpdateService.activityClass = activityClass;
        LocationUpdateService.updateStatusInterval = updateStatusInterval;

    }

    /**
     * For onCreate() should be used onCreate() and onStart() methods of Activity/Fragment lifecycle
     */
    @Override
    public void onCreate() {
        locationDelegate = new LocationDelegate(LocationUpdateService.this, activityClass, this);
        locationDelegate.onCreate(null);
        locationDelegate.updateLocationRequestInterval(updateStatusInterval);
        locationDelegate.showProgressDialog(false);
        locationDelegate.onStart();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        locationDelegate.startUpdates();

        // If we get killed, after returning from here, restart
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // We don't provide binding, so return null
        return null;
    }

    /**
     * For onDestroy() should be used onStop() and onDestroy() methods of Activity/Fragment lifecycle
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        locationDelegate.stopUpdates();
        locationDelegate.onStop();
        locationDelegate.onDestroy();
    }
}
