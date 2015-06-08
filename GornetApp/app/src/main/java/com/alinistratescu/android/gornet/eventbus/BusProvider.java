package com.alinistratescu.android.gornet.eventbus;

/**
 * Created by Catalin Matusa on 23.03.2015.
 */

import com.squareup.otto.Bus;

/**
 * Singleton provider for the event bus
 */
public class BusProvider {

    /**
     * Class instance
     */
    private static BusProvider sInstance;

    /**
     * The provided event bus
     */
    private Bus bus;

    /**
     * Static method for instantiating this provider.
     * @return an instance of this class
     */
    public static BusProvider getInstance() {
        if (sInstance == null) {
            /**
             * this provider uses an event bus to post to from any thread (main or background) and
             * receive on the main thread
             */
            sInstance = new BusProvider(new MainThreadBus());
        }

        return sInstance;
    }

    /**
     * Constructor
     *
     * @param bus the event bus provided
     */
    private BusProvider(Bus bus) {
        this.bus = bus;
    }

    /**
     * Getter
     */

    public Bus getBus() {
        return this.bus;
    }

}
