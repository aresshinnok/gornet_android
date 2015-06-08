package com.alinistratescu.android.gornet.activitydelegates.location.callbacks;

/**
 * Created by Catalin Matusa on 23.03.2015.
 */

/**
 * Callback interface for address detection using geo-coding
 */
public interface AddressDetectedCallback {

    /**
     * Called when an address is detected as a result of a geo-coding process
     *
     * @param address the geo-coded address as text
     */
    public void onAddressDetected(String address);
}
