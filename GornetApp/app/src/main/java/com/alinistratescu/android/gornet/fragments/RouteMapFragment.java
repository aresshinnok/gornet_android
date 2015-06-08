package com.alinistratescu.android.gornet.fragments;

import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
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
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.alinistratescu.android.gornet.R;
import com.alinistratescu.android.gornet.activitydelegates.location.LocationDelegate;
import com.alinistratescu.android.gornet.activitydelegates.location.callbacks.LocationChangedCallback;
import com.alinistratescu.android.gornet.base.BaseFragment;
import com.alinistratescu.android.gornet.db.models.StoreLocationModel;
import com.alinistratescu.android.gornet.eventbus.BusProvider;
import com.alinistratescu.android.gornet.eventbus.LocationUpdatedEvent;
import com.alinistratescu.android.gornet.routemaps.AbstractRouting;
import com.alinistratescu.android.gornet.routemaps.Route;
import com.alinistratescu.android.gornet.routemaps.Routing;
import com.alinistratescu.android.gornet.routemaps.RoutingListener;
import com.alinistratescu.android.gornet.services.LocationUpdateService;
import com.alinistratescu.android.gornet.utils.Constants;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;

/**
 * Created by Alin on 5/4/2015.
 */
public class RouteMapFragment extends BaseFragment implements CustomMapFragment.MapCallbacks, RoutingListener,
        LocationChangedCallback {

    private ImageButton btnLocate;
    private GoogleMap mMap;

    private LatLng clientPosition;

    private MarkerOptions marker;
    private Location currentLocation;

    private AbstractRouting.TravelMode travelMode = AbstractRouting.TravelMode.WALKING;

    private LatLngBounds.Builder builder = new LatLngBounds.Builder();

    private LocationUpdateService locationUpdateService;
    private Intent updateLocationServiceIntent;

    private StoreLocationModel currenTerminalLocation;

    /**
     * Local subscribers for the Otto event bus
     */
    private LocalSubscribers localSubscribers;

    /**
     * Default interval time for updating the location
     */
    private static long updateStatusInterval = 15000L;

    /**
     * The location delegate used to get current locations
     */
    private static LocationDelegate locationDelegate;



    private ArrayList<LatLng> waypoints;

    /**
     * Returns a new instance of this fragment with the given title.
     */
    public static RouteMapFragment newInstance(StoreLocationModel clientLocation) {
        RouteMapFragment fragment = new RouteMapFragment();
        Bundle args = new Bundle();
        args.putParcelable(Constants.ARG_NEW_REQUEST_CLIENT_LOCATION, clientLocation);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        localSubscribers = new LocalSubscribers();
        locationUpdateService = new LocationUpdateService();
        updateLocationServiceIntent = new Intent(getActivity(), LocationUpdateService.class);
        // init location delegate
        locationDelegate = new LocationDelegate(getActivity(), getActivity().getClass(), this);
        locationDelegate.onCreate(null);
        locationDelegate.showProgressDialog(false);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_order_map, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (getArguments() != null) {
            currenTerminalLocation = getArguments().getParcelable(Constants.ARG_NEW_REQUEST_CLIENT_LOCATION);
        }

        if (currenTerminalLocation != null){
            clientPosition = new LatLng(currenTerminalLocation.getLatitudine(), currenTerminalLocation.getLongitudine());
        }else{
            clientPosition = new LatLng(Constants.BUCHAREST_FAKE_KM_0_LATITUDE, Constants.BUCHAREST_FAKE_KM_0_LONGITUDE);
        }

        waypoints = new ArrayList<>();

        try {
            MapsInitializer.initialize(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnLocate = (ImageButton) view.findViewById(R.id.btnLocate);
        btnLocate.setOnClickListener(btnLocateListener);
        btnLocate.setOnClickListener(btnLocateListener);
        btnLocate.setVisibility(View.GONE);

        startProgress();
        setUpMapIfNeeded();
    }

    @Override
    public void onStart() {
        super.onStart();
        BusProvider.getInstance().getBus().register(localSubscribers);
        // stop update location service
        getActivity().stopService(updateLocationServiceIntent);

        // start location service
        LocationUpdateService.startServiceWithLoopTime(getActivity(), getActivity().getClass(), updateLocationServiceIntent, updateStatusInterval);
        // start location delegate
        locationDelegate.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onStop() {
        BusProvider.getInstance().getBus().unregister(localSubscribers);
        // stop location delegate
        locationDelegate.stopUpdates();
        locationDelegate.onStop();

        super.onStop();
    }

    @Override
    public void onDestroy() {
        // location destroy
        locationDelegate.onDestroy();

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
        return null;
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
                setZoomLevel(location);

                drawRoute(new LatLng(location.getLatitude(), location.getLongitude()), clientPosition, travelMode, waypoints);
            } else { // No location available => CENTER BUCHAREST
                centerMapToLocation(new LatLng(Constants.BUCHAREST_FAKE_KM_0_LATITUDE, Constants.BUCHAREST_FAKE_KM_0_LONGITUDE), Constants.DEFAULT_ZOOM_LEVEL); // TODO da-i locatia in functie de ce oras are selectat in dropdown
            }
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

    private void drawRoute(LatLng start, LatLng end, AbstractRouting.TravelMode travel, ArrayList<LatLng> waypoints) {
        Routing routing = new Routing(travel, waypoints);
        routing.registerListener(this);
        routing.execute(start, end);


    }

    private void setZoomLevel(Location location) {
        if (clientPosition != null) {
            builder.include(clientPosition);
            builder.include(new LatLng(location.getLatitude(), location.getLongitude()));
            final LatLngBounds bounds = builder.build();
            final int padding = 150;
            mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
                @Override
                public void onMapLoaded() {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, padding));
                }
            });
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

    @Override
    public void onLocationChanged(Location newLocation) {
        // stop location updates
        locationDelegate.stopUpdates();

        // set back the click listener
        btnLocate.setOnClickListener(btnLocateListener);

        // update location
        doLocate(newLocation);
        updateMarkerOnMap(newLocation);
    }

    private View.OnClickListener btnLocateListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            doLocate(currentLocation);
        }
    };

    @Override
    public void onRoutingFailure() {
        // The Routing request failed
    }

    @Override
    public void onRoutingStart() {
        // The Routing Request starts
    }

    @Override
    public void onRoutingSuccess(PolylineOptions mPolyOptions, Route route) {
        if (getActivity() != null) {
            PolylineOptions polyOptions = new PolylineOptions();
            polyOptions.color(getResources().getColor(R.color.gray_bdbbbc));
            polyOptions.width(10);
            polyOptions.addAll(mPolyOptions.getPoints());
            mMap.addPolyline(polyOptions);

            Log.d("DURATIONTIME: ", route.getDurationText() + " minutes");
            Log.d("DURATIONTIME: " , route.getDistanceText() + " km");

            // start marker
            MarkerOptions options;

            //waypoints
            for (int i = 0; i < waypoints.size(); i++) {
                options = new MarkerOptions();
                if (waypoints.get(i) != null) {
                    mMap.addMarker(options.position(waypoints.get(i)).icon(BitmapDescriptorFactory.fromResource(R.drawable.ico_marker)));
                }
           }

            // end marker
            options = new MarkerOptions();
            options.position(clientPosition);

            if (currenTerminalLocation != null){
                options.title(currenTerminalLocation.getTitlu());
                options.snippet(currenTerminalLocation.getAdresa());
            }

            //options.anchor(0.5f, 0.5f);
            options.icon(BitmapDescriptorFactory.fromResource(R.drawable.ico_marker));
            mMap.addMarker(options);
        }
    }

    private class LocalSubscribers {
        @Subscribe
        public void onLocationUpdatedEvent(LocationUpdatedEvent event) {
            stopProgress();

            // set current location
            currentLocation = event.getLocation();

            updateMarkerOnMap(currentLocation);
            doLocate(currentLocation);
        }
    }
}

