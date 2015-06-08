package com.alinistratescu.android.gornet.activitydelegates.location.callbacks;

/**
 * Created by Catalin Matusa on 23.03.2015.
 */

import android.location.Location;

/**
 * Callback interface for location detection
 */
public interface LocationChangedCallback {

    /**
     * Method called when a location update is detected (automatically or as a result of a
     * manual request)
     *
     * @param newLocation the updated location
     */
    public void onLocationChanged(Location newLocation);
}
