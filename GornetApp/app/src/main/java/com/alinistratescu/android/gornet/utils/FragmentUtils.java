package com.alinistratescu.android.gornet.utils;

import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * Created by Catalin Matusa on 24.03.2015.
 */
public class FragmentUtils {

    public static <T> T castToCallbackInterface(Fragment fragment, Class<T> callbackInterface) {
        Fragment parentFragment = fragment.getParentFragment();
        if (callbackInterface.isInstance(parentFragment)) {
            return (T) parentFragment;
        }

        Activity activity = fragment.getActivity();
        if (callbackInterface.isInstance(activity)) {
            return (T) activity;
        }

        return null;
    }

}
