package com.alinistratescu.android.gornet.navigationdrawer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Configuration;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.alinistratescu.android.gornet.R;
import com.alinistratescu.android.gornet.GornetApplication;
import com.alinistratescu.android.gornet.activitydelegates.location.LocationDelegate;
import com.alinistratescu.android.gornet.activitydelegates.location.callbacks.AddressDetectedCallback;
import com.alinistratescu.android.gornet.activitydelegates.location.callbacks.LocationChangedCallback;
import com.alinistratescu.android.gornet.adapters.NavigationDrawerMenuListAdapter;
import com.alinistratescu.android.gornet.base.BaseActivity;
import com.alinistratescu.android.gornet.base.BaseFragment;
import com.alinistratescu.android.gornet.db.DatabaseUtils;
import com.alinistratescu.android.gornet.db.models.StoreLocationModel;
import com.alinistratescu.android.gornet.menu.AllLocationMapMenuFragment;
import com.alinistratescu.android.gornet.menu.ContactFragmentMenu;
import com.alinistratescu.android.gornet.menu.NearMeLocationMenuFragment;
import com.alinistratescu.android.gornet.menu.ServiceFragmentMenu;
import com.alinistratescu.android.gornet.menu.TransportFragment;
import com.alinistratescu.android.gornet.rest.ApiService;
import com.alinistratescu.android.gornet.rest.RestClient;
import com.alinistratescu.android.gornet.utils.Constants;
import com.alinistratescu.android.gornet.utils.Logger;

import java.util.List;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

/**
 * Created by Alin on 5/26/2015.
 */
public class NavigationDrawerActivity extends BaseActivity implements LocationChangedCallback, AddressDetectedCallback, NearMeLocationMenuFragment.StoreLocationsFragmentCallbacks, AllLocationMapMenuFragment.StoreLocationsFragmentCallbacks {
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mDrawerToggle;
    private ListView mDrawerList;
    private NavigationDrawerMenuListAdapter adapter;
    private long backTS = 0;
    private Toolbar toolbar;

    private LocationDelegate locationDelegate;

    //private CountDownLatch latch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.drawer_layout);


        locationDelegate = new LocationDelegate(this, ((Activity) this).getClass(), this, this);
        locationDelegate.onCreate(savedInstanceState);
        if (getSharedPreferences(Constants.PREFS_BASE, 0).getLong(Constants.PREFS_SETTINGS_DOWNLOADED_DB_TIME, 0L) + Constants.ONE_DAY_TIME_IN_MILIS < System.currentTimeMillis()) {
            //clears data
           // GornetApplication.getInstance().clearApplicationData();
           // getAllLocations();
        }

        initViews();
        mDrawerList = (ListView) findViewById(R.id.left_drawer);

        adapter = new NavigationDrawerMenuListAdapter(this);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(new SlideMenuClickListener());
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        //set first item selected
        mDrawerList.setSelection(0);
        displayView(0);
    }

    public void initViews() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {
            setSupportActionBar(toolbar);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowHomeEnabled(true);
            }
        }

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);


        toolbar.setTitleTextColor(getResources().getColor(android.R.color.white));
        setSupportActionBar(toolbar);

        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, toolbar, R.string.app_name, R.string.app_name) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                //getActionBar().setTitle(mTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                super.onDrawerSlide(drawerView, 0);
                //getActionBar().setTitle(mDrawerTitle);
                //invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, 0); // this disables the animation
            }
        };

        mDrawerToggle.syncState();
    }

    @Override
    public void onAddressDetected(String address) {

    }

    /**
     * Slide menu item click listener
     */
    private class SlideMenuClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            // display view for selected nav drawer item
            displayView(position);
        }
    }

    /**
     * Diplaying fragment view for selected nav drawer list item
     */
    private void displayView(int position) {
        // update the main content by replacing fragments
        Fragment fragment = null;
        switch (position) {
            case Constants.MENU_IDX_INFO:
                fragment = new Fragment();
                break;
            case Constants.MENU_IDX_NOTIFICATION:
                fragment = new Fragment();
                break;
            case Constants.MENU_IDX_NEWS:
                fragment = new Fragment();
                break;
            case Constants.MENU_IDX_BUY_SELL:
                fragment = new Fragment();
                break;
            case Constants.MENU_IDX_MAP:
                fragment = new AllLocationMapMenuFragment();
                break;
            case Constants.MENU_IDX_PRODUCT_OF_THE_DAY:
                fragment = new Fragment();
                break;
            case Constants.MENU_IDX_EVENTS:
                fragment = new Fragment();
                break;
            case Constants.MENU_IDX_GALLERY:
                fragment = new Fragment();
                break;
            case Constants.MENU_IDX_OTHER_GORNET:
                fragment = new Fragment();
                break;
            case Constants.MENU_IDX_CONTACT:
                fragment = new Fragment();
                break;
            case Constants.MENU_IDX_PLACE_TO_SEE:
                fragment = new Fragment();
                break;
            case Constants.MENU_IDX_SOCCER_TEAM:
                fragment = new Fragment();
                break;
            case Constants.MENU_IDX_WEATHER:
                fragment = new Fragment();
                break;
            case Constants.MENU_IDX_TRANSPORT:
                fragment = new TransportFragment();
                break;
            case Constants.MENU_IDX_HOLLYDAY:
                fragment = new Fragment();
                break;
            case Constants.MENU_IDX_PROFILE:
                fragment = new Fragment();
                break;

            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();

            // update selected item and title, then close the drawer
            mDrawerList.setItemChecked(position, true);
            mDrawerList.setSelection(position);
            toolbar.setTitle(getResources().getStringArray(R.array.menu_titles)[position]);
            mDrawerLayout.closeDrawer(mDrawerList);
        } else {
            Logger.d(Constants.TAG + " No fragment created.");
        }
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        // Pass any configuration change to the drawer toggls
        mDrawerToggle.onConfigurationChanged(newConfig);
    }


    // Checks if the drawer is opened
    public boolean isDrawerOpen() {
        return mDrawerLayout != null && mDrawerLayout.isDrawerOpen(mDrawerList);
    }

    public ListView getDrawer() {
        return mDrawerList;
    }


    @Override
    public void onBackPressed() {

        // create a 2 click back button session to close app
        if (System.currentTimeMillis() < backTS + Constants.TIME_BETWEEN_CLICKS) {
            finish();
        } else {
            Toast.makeText(this, getString(R.string.press_again_to_exit), Toast.LENGTH_SHORT).show();
            backTS = System.currentTimeMillis();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        locationDelegate.onStart();
    }

    @Override
    public String getGAScreenName() {
        return null;
    }

    @Override
    protected void onResume() {
        super.onResume();
        locationDelegate.onResume();
    }

    @Override
    protected void onPause() {
        locationDelegate.onPause();
        super.onPause();
    }

    @Override
    protected void onStop() {
        locationDelegate.onStop();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        locationDelegate.onActivityResult(requestCode, resultCode, data);
    }

    public void requestLocation() {
        locationDelegate.startUpdates();
    }

    public void getAddress(Location location) {
        locationDelegate.getAddress(location);
    }

    @Override
    public void onUserLocationRequested() {
        locationDelegate.startUpdates();
    }

    public void processNewLocation(Location newLocation) {
        try {
            BaseFragment fragment = getCurrentBaseFragment();
            if (fragment != null && fragment instanceof NearMeLocationMenuFragment && fragment.isAdded()) {
                ((NearMeLocationMenuFragment) fragment).processUserLocation(newLocation);
            } else if (fragment != null && fragment instanceof AllLocationMapMenuFragment && fragment.isAdded()) {
                ((AllLocationMapMenuFragment) fragment).processUserLocation(newLocation);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public BaseFragment getCurrentBaseFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        return (BaseFragment) fragmentManager.findFragmentById(R.id.content_frame);
    }

    @Override
    public void onLocationChanged(Location newLocation) {
        // Obtaining location is done on demand; so after detection is complete, location updates are stopped
        locationDelegate.stopUpdates();

        if (newLocation != null) {
            Log.d(Constants.TAG + ".LocationChanged", newLocation.getLatitude() + ", " + newLocation.getLongitude());
        }

        // process the location received
        processNewLocation(newLocation);
    }

    public void getAllLocations() {

        startProgress();
        RestClient.getInstance(this).getHostAdapter(Constants.LOCATION_URL).create(ApiService.class).getTerminalsLocations(new Callback<List<StoreLocationModel>>() {
                                                                                                                               @Override
                                                                                                                               public void success(List<StoreLocationModel> storeLocationModels, Response response) {
                                                                                                                                   DatabaseUtils.deleteLocations(NavigationDrawerActivity.this);
//                                                                                   for (int i = 0; i < storeLocationModels.size(); i++) {
//                                                                                       DatabaseUtils.saveStoreLocation(NavigationDrawerActivity.this, storeLocationModels.get(i));
//                                                                                   }
                                                                                                                                   // DatabaseUtils.saveStoresLocations(NavigationDrawerActivity.this, storeLocationModels);
                                                                                                                                   //stopProgress();

                                                                                                                                   new UpdateDatabaseOperation(storeLocationModels).execute();
                                                                                                                                   getSharedPreferences(Constants.PREFS_BASE, 0).edit().putLong(Constants.PREFS_SETTINGS_DOWNLOADED_DB_TIME, System.currentTimeMillis()).commit();
                                                                                                                               }

                                                                                                                               @Override
                                                                                                                               public void failure(RetrofitError error) {
                                                                                                                                   stopProgress();
                                                                                                                               }
                                                                                                                           }

        );
    }

    /**
     * Async for update database
     */


    private class UpdateDatabaseOperation extends AsyncTask<String, Void, String> {
        private List<StoreLocationModel> storeLocationModels;

        public UpdateDatabaseOperation(List<StoreLocationModel> storeLocationModels) {
            this.storeLocationModels = storeLocationModels;
        }

        @Override
        protected String doInBackground(String... params) {
            DatabaseUtils.saveStoresLocations(NavigationDrawerActivity.this, storeLocationModels);
            return "Executed";
        }

        @Override
        protected void onPostExecute(String result) {
            stopProgress();
        }


        @Override
        protected void onPreExecute() {
            startProgress();
        }
    }

}
