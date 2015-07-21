package com.alinistratescu.android.gornet.eventbus;

/**
 * Created by Catalin Matusa on 23.03.2015.
 */

import android.os.Handler;
import android.os.Looper;

import com.squareup.otto.Bus;

/**
 * Event bus to post to from any thread (main or background) and receive on the main thread
 */
public class MainThreadBus extends Bus {

    private final Handler mHandler = new Handler(Looper.getMainLooper());

    @Override
    public void post(final Object event) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            super.post(event);
        } else {
            mHandler.post(new Runnable() {
                @Override
                public void run() {
                    MainThreadBus.super.post(event);
                }
            });
        }
    }

}
