package com.alinistratescu.android.gornet.base;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.view.MenuItem;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import com.alinistratescu.android.gornet.R;
import com.alinistratescu.android.gornet.activitydelegates.progress.ProgressDelegate;
import com.alinistratescu.android.gornet.base.ga.GAActionBarActivity;
import com.alinistratescu.android.gornet.navigationdrawer.NavigationDrawerActivity;

/**
 * Created by Alin on 5/25/2015.
 */
public abstract class BaseActivity extends GAActionBarActivity{
    private ProgressDelegate progressDelegate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(ImageLoaderConfiguration.createDefault(this));

        if (useProgressDelegate()) {
            // Initialize progress delegate
            progressDelegate = new ProgressDelegate(this, BaseActivity.class, null, R.color.bringo_yellow);
            progressDelegate.onCreate(savedInstanceState);
        }

    }

    // Sets a custom font ActionBar title
    protected void setActionBarTitle(String title) {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(title);
    }

    // Adds back button to the ActionBar
    protected void configureActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeButtonEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.abc_tab_indicator_material);
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setBackgroundDrawable(new ColorDrawable(getResources().getColor(R.color.white)));
        actionBar.setDisplayShowCustomEnabled(true);
    }

    @Override
    public void onBackPressed() {
        closeActivity();
    }

    public void closeActivity() {
        setResult(RESULT_CANCELED);
        finish();
    }

    // Click event for the ActionBar back button
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (this instanceof NavigationDrawerActivity) {
            return super.onOptionsItemSelected(item);
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                closeActivity();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Start progress
     */
    public void startProgress() {
        try {
            if (!useProgressDelegate()) {
                return;
            }

            progressDelegate.startProgress();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Stop progress
     */
    public void stopProgress() {
        try {
            if (!useProgressDelegate()) {
                return;
            }

            if (progressDelegate != null) {
                progressDelegate.stopProgress();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {

        if (useProgressDelegate()) {
            // Destroys progress delegate
            progressDelegate.onDestroy();
            progressDelegate = null;
        }

        super.onDestroy();
    }



    /**
     * Check if this activity, or any of its subclasses is using the attached progress delegate
     *
     * @return true if progress delegate is used, false otherwise
     */
    protected boolean useProgressDelegate() {
        return true;
    }
}
