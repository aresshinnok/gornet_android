package com.alinistratescu.android.gornet.activitydelegates;

/**
 * Created by Catalin Matusa on 20.03.2015.
 */

import android.content.Intent;
import android.os.Bundle;

/**
 * Interface that emulates activity lifecycle.<br/>
 * By using it, you may delegate some desired activity lifecycle related behaviour to an object
 * that implements this interface.<br/>
 * <br/>
 * ATTENTION: Every method defined in this interface must be called in the activity method with similar name.
 */
public interface ActivityDelegate {
    /**
     * Must be called inside {@link android.app.Activity#onCreate(Bundle)} method
     *
     * @param savedInstanceState
     */
    public void onCreate(Bundle savedInstanceState);

    /**
     * Must be called inside {@link android.app.Activity#onStart()} method
     */
    public void onStart();

    /**
     * Must be called inside {@link android.app.Activity#onResume()} method
     */
    public void onResume();

    /**
     * Must be called inside {@link android.app.Activity#onPause()} method
     */
    public void onPause();

    /**
     * Must be called inside {@link android.app.Activity#onStop()} method
     */
    public void onStop();

    /**
     * Must be called inside {@link android.app.Activity#onDestroy()} method
     */
    public void onDestroy();

    /**
     * Must be called inside {@link android.app.Activity#onActivityResult(int, int, Intent)} method
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data);

    /**
     * Must be called inside {@link android.app.Activity#onSaveInstanceState(Bundle)} method
     */
    public void onSaveInstanceState(Bundle outState);
}
