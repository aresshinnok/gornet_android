package com.alinistratescu.android.gornet.fragments;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;

import com.alinistratescu.android.gornet.utils.FragmentUtils;



public class CustomMapFragment extends SupportMapFragment {

    private View mOriginalContentView;
    private TouchableWrapper mTouchView;

    private boolean interactionEnabled = true;

    public interface MapCallbacks {
        public void onMapDragged();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        mOriginalContentView = super.onCreateView(inflater, parent, savedInstanceState);
        mTouchView = new TouchableWrapper(getActivity());
        mTouchView.addView(mOriginalContentView);
        return mTouchView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public View getView() {
        return mOriginalContentView;
    }

    private class TouchableWrapper extends FrameLayout {

        private MapCallbacks mMapCallbacks;
        private LatLng mPreviousMapCenter = null;
        private LatLng mCurrentMapCenter = null;
        private Handler mHandler;

        /**
         * Flag indicating if the user is interacting with the map
         */
        private boolean userInteractionInProgress = false;

        /**
         * Flag indicating if the user performed any interaction with the map
         */
        private boolean userPerformedAnyInteraction = false;

        public TouchableWrapper(Context context) {
            super(context);

            try {
                mMapCallbacks = FragmentUtils.castToCallbackInterface(CustomMapFragment.this, MapCallbacks.class);
            } catch (Exception e) {
                e.printStackTrace();
            }

            mHandler = new Handler();

            /**
             * called whenever the map camera is changing (drag, zoom, etc)
             */
            if (getMap() != null) {
                getMap().setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
                    @Override
                    public void onCameraChange(CameraPosition cameraPosition) {
                        // while the user is interacting with the map
                        if (userInteractionInProgress) {
                            // skip the drag event end check
                            return;
                        }

                        // if the user had at least one interaction (we are skipping any checks that
                        // could be fired when the map is first displayed on the screen)
                        if (userPerformedAnyInteraction) {
                            // a drag end event check can be made
                            checkIfMapDragEnded(100);
                        }
                    }
                });
            }
        }

        @Override
        public boolean dispatchTouchEvent(MotionEvent ev) {
            if (!isInteractionEnabled()) {
                return false;
            }

            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    // the user started an interaction with the map
                    userInteractionInProgress = true;

                    // the user had at least one interaction with the map
                    userPerformedAnyInteraction = true;

                    // save the center of the map for the drag event start
                    mPreviousMapCenter = getMap().getCameraPosition().target;

                    break;
                case MotionEvent.ACTION_UP:
                    // the user ended the interaction with the map
                    userInteractionInProgress = false;

                    // immediately check if map drag ended
                    checkIfMapDragEnded(100);

                    break;
            }

            return super.dispatchTouchEvent(ev);
        }

        private void checkIfMapDragEnded(long millis) {
            mHandler.removeCallbacks(checkRunnable);
            // update current map center
            mCurrentMapCenter = getMap().getCameraPosition().target;
            // check if the map is still moving
            mHandler.postDelayed(checkRunnable, millis);
        }

        private Runnable checkRunnable = new Runnable() {
            @Override
            public void run() {
                // if the current map center is different from the previous map center (the map is
                // still moving)
                if (!isTheSameLatLngPoint(mPreviousMapCenter, mCurrentMapCenter)) {
                    mPreviousMapCenter = mCurrentMapCenter;

                    // check again later
                    checkIfMapDragEnded(100);
                } else {
                    // the drag event is completed
                    mHandler.removeCallbacks(checkRunnable);
                    mMapCallbacks.onMapDragged();
                }
            }
        };
    }

    private boolean isTheSameLatLngPoint(LatLng p1, LatLng p2) {
        return p1 == null && p2 == null || !(p1 == null || p2 == null) && p1.latitude == p2.latitude && p1.longitude == p2.longitude;

    }

    public boolean isInteractionEnabled() {
        return interactionEnabled;
    }

    public void setInteractionEnabled(boolean interactionEnabled) {
        this.interactionEnabled = interactionEnabled;
    }

}
