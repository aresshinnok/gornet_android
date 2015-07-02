package com.alinistratescu.android.gornet.ui;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;

import com.alinistratescu.android.gornet.R;
import com.alinistratescu.android.gornet.base.BaseActivity;
import com.alinistratescu.android.gornet.db.models.StoreLocationModel;
import com.alinistratescu.android.gornet.fragments.RouteMapFragment;
import com.alinistratescu.android.gornet.utils.Constants;

/**
 * Created by Alin on 5/25/2015.
 */
public class NearMeMapActivity extends BaseActivity{

    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_near_me_locations);

        toolbar = (Toolbar) findViewById(R.id.toolbar);

        if (toolbar != null) {

            setSupportActionBar(toolbar);

            if (getSupportActionBar() != null) {
                getSupportActionBar().setDisplayShowHomeEnabled(true);
                getSupportActionBar().setTitle(getResources().getStringArray(R.array.menu_titles)[Constants.MENU_IDX_MAP]);
                //show left menu arrow
                getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            }


        }

        StoreLocationModel terminalLocation = getIntent().getParcelableExtra(Constants.ARG_NEW_REQUEST_CLIENT_LOCATION);
        Fragment mapFragment = RouteMapFragment.newInstance(terminalLocation);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fmlNearMeLocations, mapFragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public String getGAScreenName() {
        return "langa mine harta";
    }
}
