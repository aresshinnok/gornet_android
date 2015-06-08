package com.alinistratescu.android.gornet.base.ga;

import android.annotation.TargetApi;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;
import com.alinistratescu.android.gornet.GornetApplication;
import com.alinistratescu.android.gornet.utils.Constants;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public abstract class GAActionBarActivity extends AppCompatActivity {

	private boolean skipAutomaticGAHits = false;


    /**
     * display tracking screen name
     */
	@Override
	protected void onStart() {
		super.onStart();
		try {
			if (!skipAutomaticGAHits && getGAScreenName() != null) {
				Log.i(Constants.GA_TAG, getGAScreenName().isEmpty() ? ((Object) this).getClass().getSimpleName() : getGAScreenName());

				Tracker t = ((GornetApplication) getApplication()).getTracker(GornetApplication.TrackerName.APP_TRACKER);
				t.setScreenName(getGAScreenName().isEmpty() ? ((Object) this).getClass().getSimpleName() : getGAScreenName());
				t.send(new HitBuilders.AppViewBuilder().build());
				t.setScreenName(null);
			}
		} catch (Exception e) {
			Log.getStackTraceString(e);
		}
	}

	/**
	 * Gets the screen name to be reported to Google Analytics.
	 * 
	 * @return 
	 * 		a. the <b>screen name</b> to be reported to Google Analytics<br/>
	 *      b. <b>null</b> if the Google Analytics hit should be ignored<br/>
	 *      c. an <b>empty string</b> if the hit should be reported using the class name as the screen name
	 */
	public abstract String getGAScreenName();

	// NOTICE: set automatic Google Analytics usage
	// if automatic hits are skipped, they must be done "by hand"
	public void skipAutomaticGAHits(boolean skip) {
		this.skipAutomaticGAHits = skip;
	}

	/**
	 * Sends a hit with the screen name to be reported to Google Analytics.
	 * 
	 * @param gaScreenName which can take the following values:
	 * 		<br/><br/>
	 * 		a. the <b>screen name</b> to be reported to Google Analytics<br/>
	 *      b. <b>null</b> if the Google Analytics hit should be ignored<br/>
	 *      c. an <b>empty string</b> if the hit should be reported using the class name as the screen name
	 */
	public void sendGAHit(String gaScreenName) {
		try {
			if (gaScreenName != null) {
				Log.i(Constants.GA_TAG, gaScreenName.isEmpty() ? ((Object) this).getClass().getSimpleName() : gaScreenName);

				Tracker t = ((GornetApplication) getApplication()).getTracker(GornetApplication.TrackerName.APP_TRACKER);
				t.setScreenName(gaScreenName.isEmpty() ? ((Object) this).getClass().getSimpleName() : gaScreenName);
				t.send(new HitBuilders.AppViewBuilder().build());
				t.setScreenName(null);
			}
		} catch (Exception e) {
			Log.getStackTraceString(e);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

}
