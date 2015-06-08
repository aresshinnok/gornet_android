package com.alinistratescu.android.gornet.base;

import android.app.Activity;
import android.content.SharedPreferences;

import com.alinistratescu.android.gornet.R;
import com.alinistratescu.android.gornet.base.ga.GAFragment;
import com.alinistratescu.android.gornet.utils.Constants;

/**
 * Created by Alin on 5/25/2015.
 */
public abstract class BaseFragment extends GAFragment{
    /**
 * Flag stating if the displayed progress was started by this fragment or not
 */
private boolean progressStartedByCurrentFragment = false;

    protected abstract int getSectionNumber();


    /**
     * set actionbar name from a fragment
     */
    public void setActionbarName(){
        if (!getActivity().getSharedPreferences(Constants.PREFS_BASE, 0).getString(Constants.PREFS_SETTINGS_LAST_TITLE, "").equals(getResources().getStringArray(R.array.menu_titles)[getSectionNumber()])) {
            ((BaseActivity) getActivity()).getSupportActionBar().setTitle(getResources().getStringArray(R.array.menu_titles)[getSectionNumber()]);
            SharedPreferences prefs = getActivity().getSharedPreferences(Constants.PREFS_BASE, 0);
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(Constants.PREFS_SETTINGS_LAST_TITLE, getResources().getStringArray(R.array.menu_titles)[getSectionNumber()]);
            editor.commit();
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        setActionbarName();
    }

    /**
     * Read in if the progress was started by this fragment
     * @return
     */
    public boolean isProgressStartedByCurrentFragment() {
        return progressStartedByCurrentFragment;
    }


    /**
     * Keep a note if the progress was started by this fragment
     *
     * @param progressStartedByCurrentFragment
     */
    public void setProgressStartedByCurrentFragment(boolean progressStartedByCurrentFragment) {
        this.progressStartedByCurrentFragment = progressStartedByCurrentFragment;
    }

    /**
     * Start progress
     */
    public void startProgress() {
        if (getActivity() instanceof BaseActivity) {
            setProgressStartedByCurrentFragment(true);

            // Each BaseFragment is attached to a BaseActivity
            if (getActivity() instanceof BaseActivity) {
                ((BaseActivity) getActivity()).startProgress();
            }
        }
    }

    /**
     * Stop progress
     */
    public void stopProgress() {
        if (getActivity() instanceof BaseActivity) {
            setProgressStartedByCurrentFragment(false);

            // Each BaseFragment is attached to a BaseActivity
            if (getActivity() instanceof BaseActivity) {
                ((BaseActivity) getActivity()).stopProgress();
            }
        }
    }
}
