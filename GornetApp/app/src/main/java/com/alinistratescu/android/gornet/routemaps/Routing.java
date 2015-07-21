package com.alinistratescu.android.gornet.routemaps;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

/**
 * Async Task to access the Google Direction API and return the routing data.
 * Created by Furkan Tektas on 10/14/14.
 */
public class Routing extends AbstractRouting<LatLng> {

    private ArrayList<LatLng> storesList;

    public Routing(TravelMode mTravelMode, ArrayList<LatLng> list) {
        super(mTravelMode);
        this.storesList = list;
    }

    protected String constructURL(LatLng... points) {
        LatLng start = points[0];
        LatLng dest = points[1];

        final StringBuffer mBuf = new StringBuffer(AbstractRouting.DIRECTIONS_API_URL);
        mBuf.append("origin=");
        mBuf.append(start.latitude);
        mBuf.append(',');
        mBuf.append(start.longitude);
        mBuf.append("&destination=");
        mBuf.append(dest.latitude);
        mBuf.append(',');
        mBuf.append(dest.longitude);
        mBuf.append("&waypoints="); //44.466744,26.087030|44.465702,26.128014");

        for (int i = 0; i < storesList.size(); i++) {
            mBuf.append(String.valueOf(storesList.get(i).latitude) + "," + String.valueOf(storesList.get(i).longitude));
            if (i < storesList.size() - 1) {
                mBuf.append("|");
            }
        }
        mBuf.append("&sensor=true&mode=");
        mBuf.append(_mTravelMode.getValue());

        return mBuf.toString();
    }
}
