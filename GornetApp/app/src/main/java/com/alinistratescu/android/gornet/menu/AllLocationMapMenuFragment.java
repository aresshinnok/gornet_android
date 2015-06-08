package com.alinistratescu.android.gornet.menu;

import android.app.Activity;
import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.clustering.view.DefaultClusterRenderer;
import com.alinistratescu.android.gornet.R;
import com.alinistratescu.android.gornet.activitydelegates.location.callbacks.LocationChangedCallback;
import com.alinistratescu.android.gornet.base.BaseFragment;
import com.alinistratescu.android.gornet.db.DatabaseUtils;
import com.alinistratescu.android.gornet.db.models.StoreLocationModel;
import com.alinistratescu.android.gornet.fragments.CustomMapFragment;
import com.alinistratescu.android.gornet.models.ClusterItemStoreLocation;
import com.alinistratescu.android.gornet.routemaps.AbstractRouting;
import com.alinistratescu.android.gornet.utils.Constants;

import java.util.ArrayList;

/**
 * Created by Alin on 5/27/2015.
 */
public class AllLocationMapMenuFragment extends BaseFragment implements CustomMapFragment.MapCallbacks, LocationChangedCallback {

    private ImageButton btnLocate;
    private GoogleMap mMap;
    private StoreLocationsFragmentCallbacks fragmentCallbacks;
    //private LatLng clientPosition;

    public interface StoreLocationsFragmentCallbacks {
        public void onUserLocationRequested();
    }

    private MarkerOptions marker;
    private Location currentLocation;
    private ClusterManager<ClusterItemStoreLocation> mClusterManager;


    private AbstractRouting.TravelMode travelMode = AbstractRouting.TravelMode.WALKING;


    private ArrayList<StoreLocationModel> currenTerminalLocations;


    /**
     * Default interval time for updating the location
     */
    private static long updateStatusInterval = 15000L;


    /**
     * Returns a new instance of this fragment with the given title.
     */
    public static AllLocationMapMenuFragment newInstance(ArrayList<StoreLocationModel> clientLocations) {
        AllLocationMapMenuFragment fragment = new AllLocationMapMenuFragment();
        Bundle args = new Bundle();
        args.putParcelableArrayList(Constants.ARG_NEW_REQUEST_CLIENT_LOCATION, clientLocations);
        fragment.setArguments(args);
        return fragment;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        fragmentCallbacks.onUserLocationRequested();
        if (getArguments() != null) {
            currenTerminalLocations = getArguments().getParcelableArrayList(Constants.ARG_NEW_REQUEST_CLIENT_LOCATION);
        }
        currenTerminalLocations = new ArrayList<>(DatabaseUtils.getLocationsList(getActivity()));


        try {
            MapsInitializer.initialize(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        //todo travelmode

        btnLocate = (ImageButton) view.findViewById(R.id.btnLocate);

        btnLocate.setOnClickListener(btnLocateListener);
        btnLocate.setOnClickListener(btnLocateListener);

        startProgress();
        setUpMapIfNeeded();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected int getSectionNumber() {
        return 0;
    }

    @Override
    public String getGAScreenName() {
        return "harta";
    }

    /**
     * MAP Stuff
     */

    private boolean isMapReady() {
        if (mMap == null) {
            setUpMapIfNeeded();
            if (mMap == null) {
                return false;
            }
        }
        return true;
    }

    private void setUpMapIfNeeded() {
        if (mMap == null) {
            mMap = ((CustomMapFragment) getChildFragmentManager().findFragmentById(R.id.mapFragment)).getMap();
            if (mMap != null) {
                setUpMap();
            }
        } else {
            stopProgress();
        }
    }

    private void setUpMap() {
        initMapListeners();

        // center on current location, if available
        if (mMap.isMyLocationEnabled() && mMap.getMyLocation() != null) {
            doLocate(mMap.getMyLocation());
        }

        mMap.getUiSettings().setZoomControlsEnabled(false);
        mMap.getUiSettings().setAllGesturesEnabled(true);
        mMap.setMyLocationEnabled(false);
        mMap.getUiSettings().setMyLocationButtonEnabled(false);
        mMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);


        stopProgress();
    }

    private void initMapListeners() {
        // called when location services are enabled
        mMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {
            @Override
            public void onMyLocationChange(Location arg0) {
                stopProgress();

                // center the map to GPS coordinates
                doLocate(arg0);

                // remove the listener after the first call
                mMap.setOnMyLocationChangeListener(null);
            }
        });
    }

    public void doLocate(final Location location) {
        if (isMapReady()) {
            // center on given location, if available
            if (location != null) {

                centerMapToLocation(new LatLng(location.getLatitude(), location.getLongitude()), Constants.DEFAULT_ZOOM_LEVEL);
             }
            } else { // No location available => CENTER BUCHAREST
                centerMapToLocation(new LatLng(Constants.BUCHAREST_FAKE_KM_0_LATITUDE, Constants.BUCHAREST_FAKE_KM_0_LONGITUDE), Constants.DEFAULT_ZOOM_LEVEL); // TODO da-i locatia in functie de ce oras are selectat in dropdown
            }
        }


    private AbstractRouting.TravelMode getTravelMode(int travelMode) {
        switch (travelMode) {
            case 1:
                return AbstractRouting.TravelMode.WALKING;
            case 2:
                return AbstractRouting.TravelMode.BIKING;
            case 3:
                return AbstractRouting.TravelMode.DRIVING;
            case 4:
                return AbstractRouting.TravelMode.DRIVING;
            default:
                return AbstractRouting.TravelMode.DRIVING;
        }
    }

    private void centerMapToLocation(LatLng location, int zoom) {
        try {
            if (isMapReady()) {
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(location, zoom);
                mMap.animateCamera(cameraUpdate, 200, null);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void updateMarkerOnMap(Location location) {
        if (location != null) {
            LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
            if (marker == null) {
                marker = new MarkerOptions().position(latLng).icon(BitmapDescriptorFactory.fromResource(R.drawable.ico_user));
            } else {
                marker.position(latLng);
            }

            mMap.clear();

            mMap.addMarker(marker);
        }
    }

    /**
     * END MAP Stuff
     */

    @Override
    public void onMapDragged() {

    }


    private View.OnClickListener btnLocateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doLocate(currentLocation);
        }
    };

    @Override
    public void onLocationChanged(Location newLocation) {

    }

    private void setUpClusterer() {


        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<ClusterItemStoreLocation>(getActivity(), mMap);
        mClusterManager.setRenderer(new CustomRenderer());

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        mMap.setOnCameraChangeListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);

        // Add cluster items (markers) to the cluster manager.
        addItems();
    }

    private void addItems() {
        // Add ten cluster items in close proximity, for purposes of this example.
        for (int i = 0; i < currenTerminalLocations.size(); i++) {
            ClusterItemStoreLocation offsetItem = new ClusterItemStoreLocation(currenTerminalLocations.get(i).getLatitudine(), currenTerminalLocations.get(i).getLongitudine(), i);
            mClusterManager.addItem(offsetItem);
        }
    }

    public void processUserLocation(Location location) {
        stopProgress();
        updateLocation(location);
    }

    public void updateLocation(Location location) {
        if (location != null) {

            if (isMapReady()) {
                btnLocate.setVisibility(View.VISIBLE);
                currentLocation = location;

                updateMarkerOnMap(location);

                centerMapToLocation(new LatLng(location.getLatitude(), location.getLongitude()), Constants.CUSTOM_ZOOM_LEVEL);



            }
        } else {
            btnLocate.setVisibility(View.GONE);
            centerMapToLocation(new LatLng(Constants.BUCHAREST_FAKE_KM_0_LATITUDE, Constants.BUCHAREST_FAKE_KM_0_LONGITUDE), Constants.DEFAULT_ZOOM_LEVEL); // TODO da-i locatia in functie de ce oras are selectat in dropdown
        }
        setUpClusterer();

    }

    private class CustomRenderer extends DefaultClusterRenderer<ClusterItemStoreLocation>{
        private CustomRenderer() {
            super(getActivity().getApplicationContext(), mMap, mClusterManager);
        }

        @Override
        protected void onBeforeClusterItemRendered(ClusterItemStoreLocation cluster, MarkerOptions markerOptions) {
            markerOptions.icon(BitmapDescriptorFactory.fromResource(R.drawable.ico_marker));
            markerOptions.title(currenTerminalLocations.get(cluster.getPos()).getTitlu());
            markerOptions.snippet(currenTerminalLocations.get(cluster.getPos()).getOras() + " " + currenTerminalLocations.get(cluster.getPos()).getAdresa());
        }
    }



}