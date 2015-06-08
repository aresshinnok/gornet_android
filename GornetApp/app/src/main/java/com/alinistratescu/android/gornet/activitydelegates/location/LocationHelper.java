package com.alinistratescu.android.gornet.activitydelegates.location;

/**
 * Created by Catalin Matusa on 23.03.2015.
 */

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.alinistratescu.android.gornet.R;
import com.alinistratescu.android.gornet.activitydelegates.location.callbacks.AddressDetectedCallback;
import com.alinistratescu.android.gornet.activitydelegates.location.callbacks.LocationChangedCallback;
import com.alinistratescu.android.gornet.activitydelegates.progress.ProgressDialogFragment;
import com.alinistratescu.android.gornet.navigationdrawer.NavigationDrawerActivity;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Class handling location and address detection (geo-location)
 */
public class LocationHelper implements com.google.android.gms.location.LocationListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    public static final int TIMEOUT_MILLIS = 15 * 1000; // 15 seconds

    // Context instance
    private Context context;

    // Activity class
    private Class<? extends Activity> activityClass;

    // A request to connect to LocationHelper Services
    private LocationRequest mLocationRequest;

    // Stores the current instantiation of the location client in this object
    private GoogleApiClient mGoogleApiClient;

    /*
     * Note if updates have been turned on. Starts out as "false"; is set to "true" in the
     * method handleRequestSuccess of LocationUpdateReceiver.
     */
    private boolean mUpdatesRequested = false;

    // Stores connection state
    private boolean mConnected = false;

    // The progress dialog is shown by default
    private boolean showProgressDialog = true;

    // Progress shown when GetAddressTask is running
    private ProgressDialogFragment progressDialogFragment;

    private LocationChangedCallback locationChangedCallback;
    private AddressDetectedCallback addressDetectedCallback;

    private Handler timeoutHandler;
    private TimeoutRunnable timeoutRunnable;

    // HINT: call it in onCreate()
    public LocationHelper(Context context, Class<? extends Activity> activityClass) {

        // Get context instance
        this.context = context;

        // Get current activity class
        this.activityClass = activityClass;

        // Create a new global location parameters object
        this.mLocationRequest = LocationRequest.create();

        // Set the update interval
        this.mLocationRequest.setInterval(LocationUtils.UPDATE_INTERVAL_IN_MILLISECONDS);

        // Use high accuracy
        this.mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        // Set the interval ceiling to one minute
        this.mLocationRequest.setFastestInterval(LocationUtils.FAST_INTERVAL_CEILING_IN_MILLISECONDS);

        // Note that location updates are off until the user turns them on
        this.mUpdatesRequested = false;

        // Create a new location client, using the enclosing class to handle callbacks.
        mGoogleApiClient = new GoogleApiClient.Builder(this.context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        timeoutHandler = new Handler();
        timeoutRunnable = new TimeoutRunnable();
    }

    public LocationHelper(Context context, Class<? extends Activity> activityClass, boolean requestUpdates) {
        this(context, activityClass);
        this.mUpdatesRequested = requestUpdates;
    }

    // HINT: call it in onStart()
    public void connect() {
        // Connect the client. Don't re-start any requests here; instead, wait for onResume()
        mGoogleApiClient.connect();
    }

    // HINT: call it in onResume()
    public void checkLocationUpdatesPreference() {
        // Open Shared Preferences
        SharedPreferences sharedPrefs = context.getSharedPreferences(LocationUtils.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        // Get an editor
        SharedPreferences.Editor editor = sharedPrefs.edit();

        // If the app already has a setting for getting location updates, get it
        if (sharedPrefs.contains(LocationUtils.KEY_UPDATES_REQUESTED)) {
            mUpdatesRequested = sharedPrefs.getBoolean(LocationUtils.KEY_UPDATES_REQUESTED, false);

            // Otherwise, turn off location updates until requested
        } else {
            editor.putBoolean(LocationUtils.KEY_UPDATES_REQUESTED, false);
            editor.apply();
        }
    }

    // HINT: call it in onPause()
    public void saveLocationUpdatesPreference() {
        // Open Shared Preferences
        SharedPreferences sharedPrefs = context.getSharedPreferences(LocationUtils.SHARED_PREFERENCES, Context.MODE_PRIVATE);
        // Get an editor
        SharedPreferences.Editor editor = sharedPrefs.edit();

        // Save the current setting for updates
        editor.putBoolean(LocationUtils.KEY_UPDATES_REQUESTED, mUpdatesRequested);
        editor.apply();
    }

    // HINT: call it in onStop()
    public void disconnect() {
        // If the client is connected
        if (mGoogleApiClient.isConnected()) {
            stopPeriodicUpdates();
        }

        // After disconnect() is called, the client is considered "dead".
        mGoogleApiClient.disconnect();
    }

    /**
     * The method onConnectionFailed() in LocationUpdateRemover and LocationUpdateRequester
     * may call startResolutionForResult() to start an Activity that handles Google Play
     * services problems. The result of this call returns to an Activity onActivityResult()
     * method and must be checked using this method.
     */
    // HINT: call it in onActivityResult()
    public void checkResults(int requestCode, int resultCode, Intent data) {
        // Choose what to do based on the request code
        switch (requestCode) {

            // If the request code matches the code sent in onConnectionFailed
            case LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST :

                switch (resultCode) {
                    // If Google Play services resolved the problem
                    case Activity.RESULT_OK:

                        // Log the result
                        Log.d(LocationUtils.TAG, context.getString(R.string.connected));
                        Log.d(LocationUtils.TAG, context.getString(R.string.resolved));

                        // Display the result
                        // INFO: add implementation if needed
                        break;

                    // If any other result was returned by Google Play services
                    default:
                        // Log the result
                        Log.d(LocationUtils.TAG, context.getString(R.string.disconnected));
                        Log.d(LocationUtils.TAG, context.getString(R.string.no_resolution));

                        // Display the result
                        // INFO: add implementation if needed

                        break;
                }

                // If any other request code was received
            default:
                // Report that this Activity received an unknown requestCode
                Log.d(LocationUtils.TAG, context.getString(R.string.unknown_activity_request_code, requestCode));

                break;
        }
    }

    /**
     * Verify that Google Play services is available before making a request.
     *
     * @return true if Google Play services is available, otherwise false
     */
    private boolean servicesConnected() {

        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
    //        Log.d(LocationUtils.TAG, context.getString(R.string.play_services_available));

            // Continue
            return true;
            // Google Play services was not available for some reason
        } else {
            // Display an error dialog
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, activityClass.cast(context), 0);
            if (dialog != null) {
                ErrorDialogFragment errorFragment = new ErrorDialogFragment();
                errorFragment.setDialog(dialog);

                // Show the error dialog in the DialogFragment
                try {
                    errorFragment.show(activityClass.cast(context).getFragmentManager(), LocationUtils.TAG);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return false;
        }
    }

    /**
     * Calls getLastLocation() to get the current location
     */
    public Location getLastLocation() {

        // If Google Play Services is available
        if (servicesConnected()) {

            // Get the current location
            Location currentLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            Log.d(LocationUtils.TAG, LocationUtils.getLatLng(context, currentLocation));
            return currentLocation;
        }

        return null;
    }

    /**
     * Get the address of the given location, using reverse geocoding. This only works if
     * a geocoding service is available.
     */
    public void getAddress(Location location) {

        // In Gingerbread and later, use Geocoder.isPresent() to see if a geocoder is available.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD && !Geocoder.isPresent()) {
            // No geocoder is present. Issue an error message
            Toast.makeText(context, R.string.no_geocoder_available, Toast.LENGTH_LONG).show();
            return;
        }

        if (servicesConnected()) {
            // Start the background task
            (new GetAddressTask(context)).execute(location);
        }
    }

    /**
     * Sends a request to start location updates
     */
    public void startUpdates() {
        mUpdatesRequested = true;

        if (servicesConnected()) {
            startPeriodicUpdates();
        }
    }

    /**
     * Sends a request to remove location updates request them.
     */
    public void stopUpdates() {
        mUpdatesRequested = false;

        if (servicesConnected()) {
            stopPeriodicUpdates();
        }
    }

    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

        mConnected = false;

        /*
         * Google Play services can resolve some errors it detects.
         * If the error has a resolution, try sending an Intent to
         * start a Google Play services activity that can resolve
         * error.
         */
        if (connectionResult.hasResolution()) {
            try {

                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(activityClass.cast(context), LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

                /*
                * Thrown if Google Play services canceled the original
                * PendingIntent
                */

            } catch (IntentSender.SendIntentException e) {

                // Log the error
                e.printStackTrace();
            }
        } else {

            // If no resolution is available, display a dialog to the user with the error.
            showErrorDialog(connectionResult.getErrorCode());
        }
    }

    /*
     * Called by LocationHelper Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle bundle) {
//        Log.d(LocationUtils.TAG, context.getString(R.string.connected));

        mConnected = true;

        if (mUpdatesRequested) {
            startPeriodicUpdates();
        }
    }

    @Override
    public void onConnectionSuspended(int cause) {
        mConnected = false;
        Log.d(LocationUtils.TAG, context.getString(R.string.disconnected));
    }

    /**
     * Report location updates.
     *
     * @param location The updated location.
     */
    @Override
    public synchronized void onLocationChanged(Location location) {

        // Report that the location was updated
        Log.d(LocationUtils.TAG, context.getString(R.string.location_updated));

        // stop the countdown timer fallback because a location was reported (even if it's null)
        stopTimeoutCountdown();

        if (locationChangedCallback != null) {
            // Broadcast new location to listeners
            locationChangedCallback.onLocationChanged(location);
        }
    }

    /**
     * Method called if a location update is not reported after
     * milliseconds have passed since the request was made to report a null location.<br/>
     * <br/>
     * @param location
     */
    public synchronized void onTimeoutLocationChanged(Location location) {

        // Report that the location was updated
        Log.d(LocationUtils.TAG, context.getString(R.string.location_update_error));

        try {
            if (activityClass.cast(context) instanceof NavigationDrawerActivity) {
                activityClass.cast(context).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(context, context.getString(R.string.location_timeout), Toast.LENGTH_LONG).show();
                    }
                });
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        onLocationChanged(location);
    }

    /**
     * In response to a request to start updates, send a request
     * to Location Services
     */
    private void startPeriodicUpdates() {
        if (showProgressDialog) {
            showProgressDialog();
        }

        if (mConnected && mGoogleApiClient.isConnected()) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            Log.d(LocationUtils.TAG, context.getString(R.string.location_requested));
        }

        startTimeoutCountdown();
    }

    /**
     * In response to a request to stop updates, send a request to
     * Location Services
     */
    private void stopPeriodicUpdates() {
        dismissProgressDialog();

        if (mConnected && mGoogleApiClient.isConnected()) {
            stopTimeoutCountdown();
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
            Log.d(LocationUtils.TAG, context.getString(R.string.location_updates_stopped));
        }
    }

    /**
     * An AsyncTask that calls getFromLocation() in the background.
     * The class uses the following generic types:
     * Location - A {@link Location} object containing the current location,
     *            passed as the input parameter to doInBackground()
     * Void     - indicates that progress units are not used by this subclass
     * String   - An address passed to onPostExecute()
     */
    protected class GetAddressTask extends AsyncTask<Location, Void, String> {

        // Store the context passed to the AsyncTask when the system instantiates it.
        private Context ctx;

        // Constructor called by the system to instantiate the task
        public GetAddressTask(Context context) {

            // Required by the semantics of AsyncTask
            super();

            // Set a Context for the background task
            this.ctx = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // Turn the indefinite activity indicator on
            showProgressDialog();
        }

        /**
         * Get a geocoding service instance, pass latitude and longitude to it, format the returned
         * address, and return the address to the UI thread.
         */
        @Override
        protected String doInBackground(Location... params) {
            /*
             * Get a new geocoding service instance, set for localized addresses. This example uses
             * android.location.Geocoder, but other geocoders that conform to address standards
             * can also be used.
             */
            Geocoder geocoder = new Geocoder(ctx, Locale.getDefault());

            // Get the current location from the input parameter list
            Location location = params[0];

            // Create a list to contain the result address
            List<Address> addresses = null;

            // Try to get an address for the current location. Catch IO or network problems.
            try {

                /*
                 * Call the synchronous getFromLocation() method with the latitude and
                 * longitude of the current location. Return at most 1 address.
                 */
                addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);

                // Catch network or other I/O problems.
            } catch (IOException exception1) {

                // Log an error and return an error message
                Log.e(LocationUtils.TAG, ctx.getString(R.string.IO_Exception_getFromLocation));

                // print the stack trace
                exception1.printStackTrace();

                // Return an error message
                return (ctx.getString(R.string.IO_Exception_getFromLocation));

                // Catch incorrect latitude or longitude values
            } catch (IllegalArgumentException exception2) {

                // Construct a message containing the invalid arguments
                String errorString = ctx.getString(R.string.illegal_argument_exception, location.getLatitude(), location.getLongitude());

                // Log the error and print the stack trace
                Log.e(LocationUtils.TAG, errorString);
                exception2.printStackTrace();

                // Return an error message
                return errorString;
            }

            // If the reverse geocode returned an address
            if (addresses != null && addresses.size() > 0) {

                // Get the first address
                Address address = addresses.get(0);

                // Format the first line of address
                String addressText = ctx.getString(R.string.address_output_string,

                        // If there's a street address, add it
                        address.getMaxAddressLineIndex() > 0 ? address.getAddressLine(0) : "",

                        // Locality is usually a city
                        address.getLocality(),

                        // The country of the address
                        address.getCountryName()
                );

                // Return the text
                return addressText;

                // If there aren't any addresses, post a message
            } else {
                return ctx.getString(R.string.no_address_found);
            }
        }

        /**
         * A method that's called once doInBackground() completes. Set the text of the
         * UI element that displays the address. This method runs on the UI thread.
         */
        @Override
        protected void onPostExecute(String address) {

            // Turn off the progress bar
            dismissProgressDialog();

            if (addressDetectedCallback != null) {
                // Broadcast detected address to listeners
                addressDetectedCallback.onAddressDetected(address);
            }
        }
    }

    /**
     * Show a dialog returned by Google Play services for the
     * connection error code
     *
     * @param errorCode An error code returned from onConnectionFailed
     */
    private void showErrorDialog(int errorCode) {

        // Get the error dialog from Google Play services
        Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(errorCode, activityClass.cast(context), LocationUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST);

        // If Google Play services can provide an error dialog
        if (errorDialog != null) {

            // Create a new DialogFragment in which to show the error dialog
            ErrorDialogFragment errorFragment = new ErrorDialogFragment();

            // Set the dialog in the DialogFragment
            errorFragment.setDialog(errorDialog);

            // Show the error dialog in the DialogFragment
            try {
                errorFragment.show(activityClass.cast(context).getFragmentManager(), LocationUtils.TAG);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Show indeterminate progress dialog
     */
    private void showProgressDialog() {
        try {
            activityClass.cast(context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isProgressVisible()) {
                        progressDialogFragment = ProgressDialogFragment.newInstance(null, R.color.bringo_yellow);
                        progressDialogFragment.show(((AppCompatActivity) activityClass.cast(context)).getSupportFragmentManager(), ProgressDialogFragment.FRAGMENT_TAG);
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Dismiss indeterminate progress dialog
     */
    private void dismissProgressDialog() {
        try {
            activityClass.cast(context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isProgressVisible()) {
                        try {
                            progressDialogFragment.dismiss();
                        } catch (Exception e) {
                            progressDialogFragment.dismissAllowingStateLoss();
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Check if the progress is visible
     *
     * @return true if progress is visible, false otherwise
     */
    private boolean isProgressVisible() {
        return progressDialogFragment != null && progressDialogFragment.isVisible();
    }

    // OnLocationChangedListener setter
    public void setLocationChangedCallback(LocationChangedCallback locationChangedCallback) {
        this.locationChangedCallback = locationChangedCallback;
    }

    // OnAddressDetectedListener setter
    public void setAddressDetectedCallback(AddressDetectedCallback addressDetectedCallback) {
        this.addressDetectedCallback = addressDetectedCallback;
    }

    // requestUpdates setter
    public void requestUpdates(boolean requestUpdates) {
        if (mUpdatesRequested == requestUpdates) {
            return;
        }

        this.mUpdatesRequested = requestUpdates;
        saveLocationUpdatesPreference();
    }

    // LocationRequest interval setter
    public void setLocationRequestUpdateInterval(long millis) {
        mLocationRequest.setInterval(millis);
    }

    // ShowProgressDialog setter
    public void showProgressDialog(boolean showProgressDialog) {
        this.showProgressDialog = showProgressDialog;
    }

    /**
     * Timeout class used as fallback if a location update is not reported after
     * <br/>
     * INFO: A null location is reported after time expiration.
     */
    private class TimeoutRunnable implements Runnable {
        @Override
        public void run() {
            stopPeriodicUpdates();
            onTimeoutLocationChanged(null); // a null location is reported
        }
    }

    /**
     * Start the countdown timer
     */
    private void startTimeoutCountdown() {
        if (timeoutHandler == null) {
            timeoutHandler = new Handler();
        }

        if (timeoutRunnable == null) {
            timeoutRunnable = new TimeoutRunnable();
        }

        timeoutHandler.postDelayed(timeoutRunnable, TIMEOUT_MILLIS);
    }

    /**
     * Stop the countdown timer
     */
    private void stopTimeoutCountdown() {
        if (timeoutHandler != null) {
            timeoutHandler.removeCallbacks(timeoutRunnable);
        }
    }
}
