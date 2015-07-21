package com.alinistratescu.android.gornet.activitydelegates.progress;

/**
 * Created by Catalin Matusa on 24.03.2015.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;

import com.alinistratescu.android.gornet.R;
import com.alinistratescu.android.gornet.activitydelegates.ActivityDelegate;


/**
 * Progress delegate class<br/>
 * An activity could delegate progress handling to this class.
 */
public class ProgressDelegate implements ActivityDelegate {

    /**
     * The context to use
     */
    private Context context;

    /**
     * The class object of the activity that delegates the progress
     */
    private Class<? extends Activity> activityClass;

    /**
     * The fragment shown when displaying the progress.<br/>
     * In fact, the fragment inflates the default or the given layout.
     */
    private DialogFragment progressDialogFragment;

    /**
     * The progress layout displayed on the screen
     */
    private Integer progressLayoutResource;

    /**
     * The progress color
     */
    private Integer progressColorResource;

    /**
     * Flag indicating if the progress background should be dimmed or not
     */
    private boolean dimBackground = true;

    /**
     * Double check flag for keeping visible state of the progress
     */
    private boolean isVisible = false;

    /**
     * Constructor
     *
     * @param context the context to use
     * @param activityClass the class object of the activity that delegates the progress
     */
    public ProgressDelegate(Context context, Class<? extends Activity> activityClass) {
        this.context = context;
        this.activityClass = activityClass;
        this.progressLayoutResource = null;
        this.progressColorResource = null;
    }

    /**
     * Constructor
     *
     * @param context the context to use
     * @param activityClass the class object of the activity that delegates the progress
     * @param progressLayoutResource the progress layout to be displayed on the screen.<br/>
     *                               The progress layout MUST CONTAIN at least the following views:<br/>
     *                               - A RelativeLayout having android:id="@+id/background" as the root element of the layout file
     *                               - A ProgressBar having android:id="@+id/progressBar" as the progress to be displayed on the screen
     * @param progressColorResource the color of the progress bar
     */
    public ProgressDelegate(Context context, Class<? extends Activity> activityClass, Integer progressLayoutResource, Integer progressColorResource) {
        this.context = context;
        this.activityClass = activityClass;
        this.progressLayoutResource = progressLayoutResource;
        this.progressColorResource = progressColorResource;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        initProgressBar(this.progressLayoutResource, this.progressColorResource);
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onDestroy() {

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    /**
     * Set the flag indicating if the progress background should be dimmed or not
     *
     * @param enabled
     */
    public void setDimBackgroundEnabled(boolean enabled) {
        this.dimBackground = enabled;
    }

    /**
     * Set progress properties
     */
    public void initProgressBar() {
        initProgressBar(null,  null);
    }

    /**
     * Set progress properties
     *
     * @param progressLayoutResource the progress layout to be displayed on the screen.<br/>
     *                               The progress layout MUST CONTAIN at least the following views:<br/>
     *                               - A RelativeLayout having android:id="@+id/background" as the root element of the layout file
     *                               - A ProgressBar having android:id="@+id/progressBar" as the progress to be displayed on the screen
     * @param progressColorResource the color of the progress bar
     */
    public void initProgressBar(Integer progressLayoutResource, Integer progressColorResource) {
        if (progressLayoutResource == null) {
            this.progressLayoutResource = R.layout.dialog_fragment_indeterminate_circular_progress; // default progress layout
        }

        if (progressColorResource == null) {
            this.progressColorResource = android.R.color.white; // default progress color
        }
    }

    /**
     * Start progress
     */
    public void startProgress() {
        try {
            activityClass.cast(context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isProgressVisible() && !isVisible) {
                        progressDialogFragment = ProgressDialogFragment.newInstance(progressLayoutResource, progressColorResource, dimBackground);
                        progressDialogFragment.show(((AppCompatActivity) activityClass.cast(context)).getSupportFragmentManager(), ProgressDialogFragment.FRAGMENT_TAG);
                        isVisible = true;
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            isVisible = false;
        }
    }

    /**
     * Dismiss progress
     */
    public void stopProgress() {
        try {
            activityClass.cast(context).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (isProgressVisible() || isVisible) {
                        try {
                            progressDialogFragment.dismiss();
                            isVisible = false;
                        } catch (Exception e) {
                            progressDialogFragment.dismissAllowingStateLoss();
                            isVisible = false;
                        }
                    }
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
            isVisible = false;
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
}
