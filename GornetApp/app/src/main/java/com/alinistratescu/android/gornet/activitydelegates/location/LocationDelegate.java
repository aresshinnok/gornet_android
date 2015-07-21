package com.alinistratescu.android.gornet.activitydelegates.location;

/**
 * Created by Catalin Matusa on 23.03.2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;

import com.alinistratescu.android.gornet.activitydelegates.ActivityDelegate;
import com.alinistratescu.android.gornet.activitydelegates.location.callbacks.AddressDetectedCallback;
import com.alinistratescu.android.gornet.activitydelegates.location.callbacks.LocationChangedCallback;


/**
 * Location delegate class<br/>
 * An activity could delegate location detection and geo-coding handling to this class.
 */
public class LocationDelegate implements ActivityDelegate {

    /**
     * The context to use
     */
    private Context context;

    /**
     * The class object of the activity that delegates the location management
     */
    private Class<? extends Activity> activityClass;

    /**
     * Location and address detection (geo-location) handler
     */
    private LocationHelper locationHelper;

    /**
     * Callback interface for location changed (updated)
     */
    private LocationChangedCallback onLocationChangedListener;

    /**
     * Callback interface for address detection from location
     */
    private AddressDetectedCallback onAddressDetectedListener;

    /**
     * Constructor
     *
     * @param context the context to use
     * @param activityClass the class object of the activity that delegates the location management
     */
    public LocationDelegate(Context context, Class<? extends Activity> activityClass) {
        this.context = context;
        this.activityClass = activityClass;
        onLocationChangedListener = null;
        onAddressDetectedListener = null;
    }

    /**
     * Constructor
     *
     * @param context the context to use
     * @param activityClass the class object of the activity that delegates the location management
     * @param onLocationChangedListener callback for location changed (updated)
     */
    public LocationDelegate(Context context, Class<? extends Activity> activityClass, LocationChangedCallback onLocationChangedListener) {
        this.context = context;
        this.activityClass = activityClass;
        this.onLocationChangedListener = onLocationChangedListener;
        onAddressDetectedListener = null;
    }

    /**
     * Constructor
     *
     * @param context the context to use
     * @param activityClass the class object of the activity that delegates the location management
     * @param onLocationChangedListener callback for location changed (updated)
     * @param onAddressDetectedListener callback for address detection from location
     */
    public LocationDelegate(Context context, Class<? extends Activity> activityClass, LocationChangedCallback onLocationChangedListener, AddressDetectedCallback onAddressDetectedListener) {
        this.context = context;
        this.activityClass = activityClass;
        this.onLocationChangedListener = onLocationChangedListener;
        this.onAddressDetectedListener = onAddressDetectedListener;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        locationHelper = new LocationHelper(context, activityClass);
        locationHelper.setLocationChangedCallback(onLocationChangedListener);
        locationHelper.setAddressDetectedCallback(onAddressDetectedListener);
    }

    @Override
    public void onStart() {
        locationHelper.connect();
    }

    @Override
    public void onResume() {
        locationHelper.checkLocationUpdatesPreference();
    }

    @Override
    public void onPause() {
        locationHelper.saveLocationUpdatesPreference();
        try {
            locationHelper.stopUpdates();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onStop() {
        locationHelper.disconnect();
    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        locationHelper.checkResults(requestCode, resultCode, data);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    /**
     * Start location updates
     */
    public void startUpdates() {
        locationHelper.startUpdates();
    }

    /**
     * Stop location updates
     */
    public void stopUpdates() {
        locationHelper.stopUpdates();
    }

    public void updateLocationRequestInterval(long millis) {
        locationHelper.setLocationRequestUpdateInterval(millis);
    }
    public void showProgressDialog(boolean showProgressDialog) {
        locationHelper.showProgressDialog(showProgressDialog);
    }

    /**
     * Transform a location object to address using a geo-location process
     *
     * @param location the given location
     */
    public void getAddress(Location location) {
        locationHelper.getAddress(location);
    }
}
