package com.alinistratescu.android.gornet.menu;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.alinistratescu.android.gornet.R;
import com.alinistratescu.android.gornet.adapters.NearLocationsAdapter;
import com.alinistratescu.android.gornet.base.BaseFragment;
import com.alinistratescu.android.gornet.db.DatabaseUtils;
import com.alinistratescu.android.gornet.db.models.StoreLocationModel;
import com.alinistratescu.android.gornet.ui.NearMeMapActivity;
import com.alinistratescu.android.gornet.utils.CommonUtilities;
import com.alinistratescu.android.gornet.utils.Constants;
import com.alinistratescu.android.gornet.utils.MockJson;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Alin on 5/27/2015.
 */
public class NearMeLocationMenuFragment extends BaseFragment implements NearLocationsAdapter.ViewHolder.RecycleViewItemListener {

    private ArrayList<StoreLocationModel> stores = new ArrayList<>();
    RecyclerView recList;
    NearLocationsAdapter mAdapter;
    private Location mCurrentLocation;
    private static final double FIXED_CONSTANT = 1.2;

    public interface StoreLocationsFragmentCallbacks {
        public void onUserLocationRequested();
    }

    private StoreLocationsFragmentCallbacks fragmentCallbacks;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_near_me_location, null);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupViews(view);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            // the activity to which the fragment attaches should implement FragmentCallbacks interface
            fragmentCallbacks = (StoreLocationsFragmentCallbacks) activity;
        } catch (ClassCastException e) {
            e.printStackTrace();
            fragmentCallbacks = null;
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    protected int getSectionNumber() {
        return 0;
    }

    private void setupViews(View view) {

        fragmentCallbacks.onUserLocationRequested();

        recList = (RecyclerView) view.findViewById(R.id.rvNearLocations);
        recList.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recList.setLayoutManager(llm);

        // stores = new ArrayList<>(DatabaseUtils.getLocationsList(getActivity()));

        mAdapter = new NearLocationsAdapter(getActivity(), stores, this);
        recList.setAdapter(mAdapter);
        // populateDb();
        //getAllLocations();

    }

//used to get mock locations
    public void populateDb() {

        ArrayList<StoreLocationModel> locations = new ArrayList<>();

        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .excludeFieldsWithoutExposeAnnotation() // every field used by GSON must have the @Expose annotation
                .setPrettyPrinting()
                .serializeNulls()
                .create();

        try {
            JSONArray jsonArray = new JSONArray(MockJson.mockLocations);
            for (int i = 0; i < jsonArray.length(); i++) {
                locations.add(gson.fromJson(jsonArray.optJSONObject(i).toString(), StoreLocationModel.class));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < locations.size(); i++) {
            DatabaseUtils.saveStoreLocation(getActivity(), locations.get(i));
        }


        Log.d("TAG", locations.size() + "");
    }

    @Override
    public void onRecycleItemClick(View caller, int position) {
        if (mCurrentLocation != null) {
            Intent intent = new Intent(getActivity(), NearMeMapActivity.class);
            intent.putExtra(Constants.ARG_NEW_REQUEST_CLIENT_LOCATION, stores.get(position));
            startActivity(intent);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public String getGAScreenName() {
        return "langa mine";
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    public void updateDistanceToAllLocations(final Location location) {

        ArrayList<StoreLocationModel> storeLocationModels = new ArrayList<>(DatabaseUtils.getLocationsList(getActivity()));
        for (int i = 0; i < storeLocationModels.size(); i++) {
            if (location != null) {
                Location locationto = new Location("point A");
                locationto.setLatitude(storeLocationModels.get(i).getLatitudine());
                locationto.setLongitude(storeLocationModels.get(i).getLongitudine());
                int distance = Integer.parseInt(CommonUtilities.convertMToKm(getActivity(), (int) ((location.distanceTo(locationto)) * FIXED_CONSTANT)));
                        storeLocationModels.get(i).setDistance(distance > 1 ? distance : 1);
            } else {
                storeLocationModels.get(i).setDistance(-1);
            }
        }

        if (location != null) {
            Collections.sort(storeLocationModels, new Comparator<StoreLocationModel>() {
                public int compare(StoreLocationModel loc1, StoreLocationModel loc2) {
                    return loc1.getDistance() - loc2.getDistance();
                }
            });
        }

        for (int i = 0; i < storeLocationModels.size(); i++) {

            if (location != null) {
                if (stores.size() < 30) {
                    stores.add(storeLocationModels.get(i));
                }
            } else {
                stores.addAll(storeLocationModels);
            }
        }

        mAdapter.notifyDataSetChanged();
        stopProgress();
    }

    public void processUserLocation(Location location) {
        stopProgress();
        mCurrentLocation = location;
        updateDistanceToAllLocations(location);
    }
}
