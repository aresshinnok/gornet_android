package com.alinistratescu.android.gornet.models;

import com.google.android.gms.maps.model.LatLng;
import com.google.maps.android.clustering.ClusterItem;

/**
 * Created by Alin on 5/29/2015.
 * It is used for map cluster items
 */
public class ClusterItemStoreLocation implements ClusterItem {
    private final LatLng mPosition;
    private final int pos;

    public ClusterItemStoreLocation(double lat, double lng, int pos) {
        mPosition = new LatLng(lat, lng);
        this.pos = pos;
    }


    @Override
    public LatLng getPosition() {
        return mPosition;
    }

    public int getPos() {
        return pos;
    }
}