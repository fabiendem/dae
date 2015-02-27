package com.fabiendem.defib68.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.avast.android.dialogs.fragment.SimpleDialogFragment;
import com.avast.android.dialogs.iface.ISimpleDialogListener;
import com.fabiendem.defib68.PreferencesManager;
import com.fabiendem.defib68.R;
import com.fabiendem.defib68.map.DefibrillatorClusterRenderer;
import com.fabiendem.defib68.map.DefibrillatorFinder;
import com.fabiendem.defib68.map.DefibrillatorInfoWindowAdapter;
import com.fabiendem.defib68.map.MapUtils;
import com.fabiendem.defib68.models.defibrillator.DefibrillatorClusterItem;
import com.fabiendem.defib68.models.defibrillator.DefibrillatorModel;
import com.fabiendem.defib68.models.defibrillator.json.DefibrillatorJsonConvertor;
import com.fabiendem.defib68.utils.AnimUtils;
import com.fabiendem.defib68.utils.ApplicationUtils;
import com.fabiendem.defib68.utils.HautRhinUtils;
import com.fabiendem.defib68.utils.UiUtils;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.quadtree.PointQuadTree;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nispok.snackbar.Snackbar;
import com.nispok.snackbar.SnackbarManager;
import com.nispok.snackbar.enums.SnackbarType;
import com.nispok.snackbar.listeners.ActionClickListener;
import com.nispok.snackbar.listeners.EventListener;

import java.util.HashMap;
import java.util.List;

public class MapFragment extends Fragment
        implements OnMapReadyCallback,
                    ClusterManager.OnClusterItemClickListener<DefibrillatorClusterItem>,
                    ClusterManager.OnClusterItemInfoWindowClickListener<DefibrillatorClusterItem>,
                    GoogleMap.OnMapClickListener,
                    View.OnClickListener,
                    GoogleApiClient.ConnectionCallbacks,
                    GoogleApiClient.OnConnectionFailedListener,
                    LocationListener,
                    ISimpleDialogListener {

    public static final String TAG = "MapFragment";

    private SupportMapFragment mSupportMapFragment;
    private GoogleMap mMap; // Might be null if Google Play services APK is not available.

    private ViewGroup mContainer;
    private Circle mCircleWalkingPerimeter;
    private TextView mErrorTxt;
    private ViewGroup mFabButtons;
    private ImageButton mShowMyLocationBtn;
    private ImageButton mShowHautRhinBtn;
    private ImageButton mShowClosestDefibBtn;

    private Snackbar mSnackbarNotIn68;
    private Snackbar mSnackbarLocationUnknown;

    private static final int TRANSLATE_ANIMATION_DURATION_MS = 250;

    private static final int REQUEST_CODE_ALERT_LOCATION_SERVICES_DISABLED = 0;

    private List<DefibrillatorModel> mDefibrillators;
    private DefibrillatorClusterRenderer mClusterRenderer;
    private ClusterManager<DefibrillatorClusterItem> mClusterManager;
    private PointQuadTree<DefibrillatorClusterItem> mPointQuadTree;
    private HashMap<String, DefibrillatorModel> mMapDefibrillators;

    private Marker mActiveDefibrillatorMarker;
    private Marker mClosestDefibrillatorMarker;

    // Grab location
    /*
     * Define a request code to send to Google Play services
     * This code is returned in Activity.onActivityResult
     */
    private final static int
            GP_CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

    // Milliseconds per second
    private static final int MILLISECONDS_PER_SECOND = 1000;

    // Update frequency in seconds
    public static final int UPDATE_INTERVAL_IN_SECONDS = 5;

    // Update frequency in milliseconds
    private static final long UPDATE_INTERVAL =
            MILLISECONDS_PER_SECOND * UPDATE_INTERVAL_IN_SECONDS;

    // The fastest update frequency, in seconds
    private static final int FASTEST_INTERVAL_IN_SECONDS = 1;

    // A fast frequency ceiling in milliseconds
    private static final long FASTEST_INTERVAL =
            MILLISECONDS_PER_SECOND * FASTEST_INTERVAL_IN_SECONDS;

    private GoogleApiClient mGoogleApiClient;

    // Define an object that holds accuracy and frequency parameters
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;
    private boolean mRequestingLocationUpdates;

    public MapFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupLocationClient();

        mPointQuadTree = new PointQuadTree<DefibrillatorClusterItem>(
                HautRhinUtils.LEFT_BOUND,
                HautRhinUtils.RIGHT_BOUND,
                HautRhinUtils.BOTTOM_BOUND,
                HautRhinUtils.TOP_BOUND);
        mMapDefibrillators = new HashMap<String, DefibrillatorModel>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        mContainer = container;
        mErrorTxt = (TextView) view.findViewById(R.id.error_txt);
        mFabButtons = (ViewGroup) view.findViewById(R.id.fab_buttons);
        mShowMyLocationBtn = (ImageButton) view.findViewById(R.id.show_my_location_btn);
        mShowHautRhinBtn = (ImageButton) view.findViewById(R.id.show_haut_rhin_btn);
        mShowClosestDefibBtn = (ImageButton) view.findViewById(R.id.show_closest_defib_btn);

        mShowMyLocationBtn.setOnClickListener(this);
        mShowHautRhinBtn.setOnClickListener(this);
        mShowClosestDefibBtn.setOnClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        FragmentManager fragmentManager = getChildFragmentManager();
        mSupportMapFragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
        if (mSupportMapFragment == null) {
            mSupportMapFragment = SupportMapFragment.newInstance();
            fragmentManager.beginTransaction().replace(R.id.map, mSupportMapFragment).commit();
        }

        mSupportMapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        setUpMapIfNeeded();
    }


    private void setupLocationClient() {
        mRequestingLocationUpdates = true;

        /*
         * Create a new location client, using the enclosing class to
         * handle callbacks.
         */
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        createLocationRequest();
    }

    private void createLocationRequest() {
        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create();
        // Use high accuracy
        mLocationRequest.setPriority(
                LocationRequest.PRIORITY_HIGH_ACCURACY);
        // Set the update interval to 5 seconds
        mLocationRequest.setInterval(UPDATE_INTERVAL);
        // Set the fastest update interval to 1 second
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
    }

    /*
     * Called when the Activity becomes visible.
     */
    @Override
    public void onStart() {
        super.onStart();
        if (isGooglePlayServicesAvailable(true)) {
            // Connect the client.
            mGoogleApiClient.connect();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        setUpMapIfNeeded();

        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.d(TAG, "onPause");

        // If the client is connected
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    /*
     * Called when the Activity is no longer visible.
     */
    @Override
    public void onStop() {
        Log.d(TAG, "onStop");

        /*
         * After disconnect() is called, the client is
         * considered "dead".
         */
        mGoogleApiClient.disconnect();
        super.onStop();
    }

    /*
     * Handle results returned to the FragmentActivity
     * by Google Play services
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {
            case GP_CONNECTION_FAILURE_RESOLUTION_REQUEST:
                /*
                 * If the result code is Activity.RESULT_OK, try
                 * to connect again
                 */
                switch (resultCode) {
                    case Activity.RESULT_OK:
                    /*
                     * Try the request again
                     */
                        if (isGooglePlayServicesAvailable(false)) {
                            mGoogleApiClient.connect();
                        } else {
                            // TODO: Show an error dialog explaining that GP services is missing
                            // and that it's fatal
                            Log.e(TAG, "Google play services not available");
                        }
                        break;
                }
                break;
        }
    }

    private boolean isGooglePlayServicesAvailable(boolean showErrorDialog) {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getActivity());

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d(TAG, "Google Play services is available.");
            return true;
        } else {
            if (showErrorDialog) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, getActivity(), GP_CONNECTION_FAILURE_RESOLUTION_REQUEST).show();
            }
            return false;
        }
    }

    private GoogleMap getMap() {
        return mMap;
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p/>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p/>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            //FragmentManager fragmentManager = getChildFragmentManager();
            //SupportMapFragment fragment = (SupportMapFragment) fragmentManager.findFragmentById(R.id.map);
            mMap = mSupportMapFragment.getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera.
     * <p/>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        // Map type from settings
        mMap.setMapType(PreferencesManager.getInstance(getActivity()).getMapType());


        View infoWindow = getActivity().getLayoutInflater().inflate(R.layout.info_window_content_defibrillator, null);
        mMap.setInfoWindowAdapter(new DefibrillatorInfoWindowAdapter(getActivity(), infoWindow, mMapDefibrillators));

        // Enable compass
        UiSettings uiMapSettings = mMap.getUiSettings();

        // Disable default my location button
        uiMapSettings.setMyLocationButtonEnabled(false);
        uiMapSettings.setZoomControlsEnabled(false);
        uiMapSettings.setCompassEnabled(false);
        // Remove map toolbar
        uiMapSettings.setMapToolbarEnabled(false);

        // Display my location
        mMap.setMyLocationEnabled(true);

        // Listen to clicks on the map
        mMap.setOnMapClickListener(this);

        // Add the defibrillators
        new AsyncTask<Void, Void, List<DefibrillatorModel>>() {

            @Override
            protected List<DefibrillatorModel> doInBackground(Void... values) {
                Log.d(TAG, "Loading defibs in background");
                return loadDefibrillators();
            }

            @Override
            protected void onPostExecute(List<DefibrillatorModel> defibrillatorModels) {
                Log.d(TAG, "Loading defibs in background DONE");
                super.onPostExecute(defibrillatorModels);
                mDefibrillators = defibrillatorModels;
                setupClusterer();
            }
        }.execute();

    }

    private List<DefibrillatorModel> loadDefibrillators() {
        String jsonDefibrillators = ApplicationUtils.loadJSONFromAsset(getActivity(), "defibs.json");
        List<DefibrillatorModel> jsonToDefibrillatorList = (List<DefibrillatorModel>) DefibrillatorJsonConvertor.ConvertJsonStringToDefibrillatorsCollection(jsonDefibrillators);
        return jsonToDefibrillatorList;
    }

    private void setupClusterer() {
        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<DefibrillatorClusterItem>(getActivity(), getMap());
        mClusterRenderer = new DefibrillatorClusterRenderer(getActivity(), getMap(), mClusterManager);
        mClusterManager.setRenderer(mClusterRenderer);
        mClusterManager.setOnClusterItemClickListener(this);
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        getMap().setOnCameraChangeListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);
        getMap().setOnInfoWindowClickListener(mClusterManager);

        addDefibrillatorsMarkers();

        mClusterManager.cluster();
    }

    private void addDefibrillatorsMarkers() {
        DefibrillatorClusterItem defibrillatorClusterItem;
        for (DefibrillatorModel defibrillator : mDefibrillators) {
            defibrillatorClusterItem = new DefibrillatorClusterItem(defibrillator);
            mClusterManager.addItem(defibrillatorClusterItem);
            mPointQuadTree.add(defibrillatorClusterItem);
            mMapDefibrillators.put(String.valueOf(defibrillator.getId()), defibrillator);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.show_haut_rhin_btn:
                onClickHautRhinBtn(view);
                break;
            case R.id.show_closest_defib_btn:
                onClickClosestBtn(view);
                break;
            case R.id.show_my_location_btn:
                onClickMyLocationBtn(view);
                break;
        }
    }

    private void onClickClosestBtn(View view) {
        if(! LocationUtils.isLocationServiceEnabled(getActivity())) {
            showAlertLocationServiceDisabled();
        }
        else {
            new ShowClosestDefibrillatorAsyncTask().execute();
        }
    }

    private void onClickMyLocationBtn(View view) {
        if(! LocationUtils.isLocationServiceEnabled(getActivity())) {
            showAlertLocationServiceDisabled();
        }
        else {
            if (mCurrentLocation != null) {
                MapUtils.animateCameraToCurrentLocation(mMap, mCurrentLocation);
            }
        }
    }

    private void onClickHautRhinBtn(View view) {
        MapUtils.animateCameraToHautRhin(getActivity(), mMap);
    }

    /*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "Location services connected");

        Location lastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        // Get last location
        updateCurrentLocation(lastLocation);
        if (mRequestingLocationUpdates) {
            Log.d(TAG, "Requesting location updates");
            startLocationUpdates();
        }
    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onConnectionSuspended(int i) {
        Log.w(TAG, "Location services disconnected");
    }

    /*
     * Called by Location Services if the attempt to
     * Location Services fails.
     */
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.e(TAG, "Location services connection failed");
    }

    protected void startLocationUpdates() {
        Log.d(TAG, "Start location updates");
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }

    protected void stopLocationUpdates() {
        Log.d(TAG, "Stop location updates");
        LocationServices.FusedLocationApi.removeLocationUpdates(
                mGoogleApiClient, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        Log.d(TAG, "Location changed");
        updateCurrentLocation(location);
    }

    private void updateCurrentLocation(Location location) {
        if (location != null) {
            if (mCurrentLocation != null) {
                if (mCurrentLocation.getLongitude() != location.getLongitude() ||
                        mCurrentLocation.getLatitude() != location.getLatitude()) {
                    Log.d(TAG, "Current location updated: " + location.toString());
                    mCurrentLocation = location;
                    onReallyNewCurrentLocation();
                }
                else {
                    Log.d(TAG, "Location is the same");
                }
            } else {
                Log.d(TAG, "Current location updated: " + location.toString());
                mCurrentLocation = location;
                onReallyNewCurrentLocation();
            }

            if (! HautRhinUtils.isLocationInHautRhin(mCurrentLocation)) {
                Log.d(TAG, "Not in Haut Rhin");
                showSnackbarLocationOutOf68();
            }
            else {
                Log.d(TAG, "Yes, in Haut Rhin");
                hideSnackbarLocationOutOf68();
                hideSnackbarLocationUnknown();
            }
        } else {
            Log.e(TAG, "Current location unknown");
            onLocationUnknown();
        }
    }

    private void onReallyNewCurrentLocation() {
        drawCircleWalkingPerimeter();
    }

    private void onLocationUnknown() {
        showSnackbarLocationUnknown();
    }

    @Override
    public void onMapClick(LatLng latLng) {
        resetActiveMarker();
    }

    @Override
    public boolean onClusterItemClick(DefibrillatorClusterItem defibrillatorClusterItem) {
        Log.d(TAG, "onClusterItemClick");
        // Reset previous marker
        resetActiveMarker();
        if (!mClusterRenderer.getMarker(defibrillatorClusterItem).equals(mClosestDefibrillatorMarker)) {
            highlightActiveDefibrillator(defibrillatorClusterItem);
        }
        return false;
    }

    @Override
    public void onClusterItemInfoWindowClick(DefibrillatorClusterItem item) {
        Log.d(TAG, "onClusterItemInfoWindowClick");

        Intent intentWalkingDirections = MapUtils.getIntentGoogleMap(item.getPosition());
        // Create intent to show chooser
        Intent chooserMapApplication = Intent.createChooser(intentWalkingDirections, getString(R.string.walking_directions_title));

        // Verify the intent will resolve to at least one activity
        if (intentWalkingDirections.resolveActivity(getActivity().getPackageManager()) != null) {
            PreferencesManager.getInstance(getActivity()).setHasTipInfoWindowShowDirectionsBeenShown(true);
            startActivity(chooserMapApplication);
        }
    }

    private void highlightActiveDefibrillatorMarker(Marker activeDefibrillatorMarker) {
        mActiveDefibrillatorMarker = activeDefibrillatorMarker;
        if (mActiveDefibrillatorMarker != null) {
            mActiveDefibrillatorMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_active));
        }
    }

    private void highlightActiveDefibrillator(DefibrillatorClusterItem defibrillatorClusterItem) {
        highlightActiveDefibrillatorMarker(mClusterRenderer.getMarker(defibrillatorClusterItem));
    }

    private void resetActiveMarker() {
        if (mActiveDefibrillatorMarker != null) {
            try {
                mActiveDefibrillatorMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin));
                mActiveDefibrillatorMarker = null;
            } catch (IllegalArgumentException pinRemovedException) {
                Log.d(TAG, "Marker already removed");
            }
        }
    }

    private void highlightClosestMarker(Marker closestDefibrillatorMarker) {
        mClosestDefibrillatorMarker = closestDefibrillatorMarker;
        if (mClosestDefibrillatorMarker != null) {
            mClosestDefibrillatorMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin_closest));
            mClosestDefibrillatorMarker.showInfoWindow();
        }
    }

    private void highlightClosestDefibrillator(DefibrillatorClusterItem defibrillatorClusterItem) {
        highlightClosestMarker(mClusterRenderer.getMarker(defibrillatorClusterItem));
    }

    private void resetClosestMarker() {
        if (mClosestDefibrillatorMarker != null) {
            try {
                mClosestDefibrillatorMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.pin));
                mClosestDefibrillatorMarker = null;
            } catch (IllegalArgumentException pinRemovedException) {
                Log.d(TAG, "Marker already removed");
            }
        }
    }

    private void drawCircleWalkingPerimeter() {
        LatLng currentLocationLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        if (mCircleWalkingPerimeter == null) {
            // Instantiates a new CircleOptions object and defines the center and radius
            CircleOptions circleOptions = new CircleOptions()
                    .center(currentLocationLatLng)
                    .strokeColor(getResources().getColor(R.color.primary))
                    .strokeWidth(4)
                    .fillColor(Color.argb(75, 255, 255, 255))
                    .radius(200); // In meters, 400 hundreds meters return in 5 minutes
            // Get back the mutable Circle
            mCircleWalkingPerimeter = mMap.addCircle(circleOptions);
        } else {
            mCircleWalkingPerimeter.setCenter(currentLocationLatLng);
        }
    }

    private class ShowClosestDefibrillatorAsyncTask extends AsyncTask<Void, Void, DefibrillatorClusterItem> {

        @Override
        protected DefibrillatorClusterItem doInBackground(Void... params) {
            return DefibrillatorFinder.getClosestDefibrillator(mCurrentLocation, mPointQuadTree, HautRhinUtils.getBounds());
        }

        @Override
        protected void onPostExecute(final DefibrillatorClusterItem closestDefib) {
            super.onPostExecute(closestDefib);

            if (closestDefib != null) {
                Log.d(TAG, "Got it: " + closestDefib.getLocationDescription());

                LatLng currentLocationLatLng = MapUtils.getLatLng(mCurrentLocation);
                LatLngBounds boundsCurrentLocationAndDefibrillator =
                        MapUtils.getLatLngBounds(currentLocationLatLng, closestDefib.getPosition());

                mMap.animateCamera(
                        CameraUpdateFactory.newLatLngBounds(
                                boundsCurrentLocationAndDefibrillator,
                                UiUtils.dpToPx(getActivity(), 150)),
                        new GoogleMap.CancelableCallback() {
                            @Override
                            public void onFinish() {
                                Log.d(TAG, "Map animation finished");

                                // Reset active marker
                                resetActiveMarker();

                                if (mClosestDefibrillatorMarker == null ||
                                        !mClosestDefibrillatorMarker.equals(mClusterRenderer.getMarker(closestDefib))) {
                                    Log.d(TAG, "Closest marker is null or different than new one");

                                    // Because we zoom, the marker may be rendered later on
                                    mClusterRenderer.setClusterItemRenderingListener(new DefibrillatorClusterRenderer.ClusterItemRenderingListener() {
                                        @Override
                                        public void onClusterItemRendered(DefibrillatorClusterItem defibrillatorClusterItem, Marker marker) {
                                            Log.d(TAG, "Cluster item rendered: " + defibrillatorClusterItem.getLocationDescription());

                                            if (defibrillatorClusterItem.equals(closestDefib)) {
                                                Log.d(TAG, "Marker rendered is the closest one, highlight");
                                                highlightClosestDefibrillator(closestDefib);

                                                // Self destruct
                                                mClusterRenderer.setClusterItemRenderingListener(null);
                                            }
                                        }
                                    });
                                } else {
                                    Log.d(TAG, "Active marker is still the same");
                                    highlightClosestMarker(mClosestDefibrillatorMarker);
                                }
                            }

                            @Override
                            public void onCancel() {

                            }
                        }
                );
            } else {
                Log.d(TAG, "Couldn't find closest defib");
            }
        }
    }

    private void moveUpFabButtons(int height) {
        AnimUtils.translateY(mFabButtons, 0, -height, TRANSLATE_ANIMATION_DURATION_MS);
    }

    private void moveDownFabButtons(int height) {
        AnimUtils.translateY(mFabButtons, -height, 0, TRANSLATE_ANIMATION_DURATION_MS);
    }

    private void showSnackbarLocationUnknown() {

        if(mSnackbarLocationUnknown == null ||
                ! mSnackbarLocationUnknown.isShowing()) {

            Log.d(TAG, "showSnackbarLocationUnknown");
            SnackbarManager.show(
                    Snackbar.with(getActivity().getApplicationContext())
                            .text(getString(R.string.error_unknown_location))
                            .type(SnackbarType.MULTI_LINE)
                            .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                            .eventListener(new EventListener() {
                                @Override
                                public void onShow(Snackbar snackbar) {
                                }

                                @Override
                                public void onShowByReplace(Snackbar snackbar) {
                                }

                                @Override
                                public void onShown(Snackbar snackbar) {
                                    mSnackbarLocationUnknown = snackbar;
                                    moveUpFabButtons(snackbar.getHeight());
                                }

                                @Override
                                public void onDismiss(Snackbar snackbar) {
                                    moveDownFabButtons(snackbar.getHeight());
                                }

                                @Override
                                public void onDismissByReplace(Snackbar snackbar) {
                                }

                                @Override
                                public void onDismissed(Snackbar snackbar) {
                                }
                            })
                    , mContainer); // activity where it is displayed
        }
    }

    private void hideSnackbarLocationUnknown() {
        if( mSnackbarLocationUnknown != null &&
                mSnackbarLocationUnknown.isShowing()) {
            Log.d(TAG, "hideSnackbarLocationUnknown");
            mSnackbarLocationUnknown.dismiss();
        }
    }

    private void showSnackbarLocationOutOf68() {
        if(mSnackbarNotIn68 == null ||
                ! mSnackbarNotIn68.isShowing()) {

            SnackbarManager.show(
                    Snackbar.with(getActivity().getApplicationContext())
                            .text(getString(R.string.error_not_in_68))
                            .type(SnackbarType.MULTI_LINE)
                            .duration(Snackbar.SnackbarDuration.LENGTH_INDEFINITE)
                            .eventListener(new EventListener() {
                                @Override
                                public void onShow(Snackbar snackbar) {
                                }

                                @Override
                                public void onShowByReplace(Snackbar snackbar) {
                                }

                                @Override
                                public void onShown(Snackbar snackbar) {
                                    moveUpFabButtons(snackbar.getHeight());
                                    mSnackbarNotIn68 = snackbar;
                                }

                                @Override
                                public void onDismiss(Snackbar snackbar) {
                                    moveDownFabButtons(snackbar.getHeight());
                                }

                                @Override
                                public void onDismissByReplace(Snackbar snackbar) {
                                }

                                @Override
                                public void onDismissed(Snackbar snackbar) {
                                }
                            })
                    , mContainer); // activity where it is displayed
        }
    }

    private void hideSnackbarLocationOutOf68() {
        if( mSnackbarNotIn68 != null &&
                mSnackbarNotIn68.isShowing()) {
            Log.d(TAG, "hideSnackbarLocationOutOf68");
            mSnackbarNotIn68.dismiss();
        }
    }

    private void showAlertLocationServiceDisabled() {
        SimpleDialogFragment.createBuilder(getActivity(), getActivity().getSupportFragmentManager())
                .setTitle("Services de localisation désactivés")
                .setMessage("Votre position doit être accessible pour trouver le défibrillateur le plus proche de vous.\nVeuillez activer la fonctionnalité d'accès aux données de localisation.")
                .setPositiveButtonText(getString(R.string.settings))
                .setNegativeButtonText("Ignorer")
                .setTargetFragment(this, REQUEST_CODE_ALERT_LOCATION_SERVICES_DISABLED)
                .show();
    }

    @Override
    public void onNegativeButtonClicked(int requestCode) {

    }

    @Override
    public void onNeutralButtonClicked(int requestCode) {

    }

    @Override
    public void onPositiveButtonClicked(int requestCode) {
        if(requestCode == REQUEST_CODE_ALERT_LOCATION_SERVICES_DISABLED) {
            ApplicationUtils.launchLocationSettingsIntent(getActivity());
        }
    }

    public int toggleMapType() {
        int newMapType;
        if(mMap.getMapType() != GoogleMap.MAP_TYPE_NORMAL) {
            newMapType = GoogleMap.MAP_TYPE_NORMAL;
        }
        else {
            newMapType = GoogleMap.MAP_TYPE_HYBRID;
        }
        mMap.setMapType(newMapType);
        return newMapType;
    }

    public int getMapType() {
        return mMap.getMapType();
    }
}