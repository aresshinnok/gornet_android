package com.alinistratescu.android.gornet.eventbus;

import android.location.Location;

/**
 * Created by Alin Istratescu on 27.05.2015.
 */
public class LocationUpdatedEvent {

    private Location location;
    /**
     * Constructor
     */
    public LocationUpdatedEvent(Location location) {
        setLocation(location);
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
