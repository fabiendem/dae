package com.fabiendem.defib68.activities;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fabiendem.defib68.DummyDefibrillators;
import com.fabiendem.defib68.HautRhinUtils;
import com.fabiendem.defib68.R;
import com.fabiendem.defib68.UiUtils;
import com.fabiendem.defib68.map.DefibrillatorClusterRenderer;
import com.fabiendem.defib68.map.MapUtils;
import com.fabiendem.defib68.models.EnvironmentEnum;
import com.fabiendem.defib68.models.defibrillator.DefibrillatorClusterItem;
import com.fabiendem.defib68.models.defibrillator.DefibrillatorModel;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.SphericalUtil;
import com.google.maps.android.clustering.ClusterManager;
import com.google.maps.android.geometry.Bounds;
import com.google.maps.android.quadtree.PointQuadTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapActivity extends ActionBarActivity
        implements GooglePlayServicesClient.ConnectionCallbacks,
        GooglePlayServicesClient.OnConnectionFailedListener,
        LocationListener, ClusterManager.OnClusterItemInfoWindowClickListener<DefibrillatorClusterItem> {

    public static final String TAG = "MapActivity";

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    private Polyline mWalkingPerimeterCircle;

    private Circle mCircleWalkingPerimeter;
    private TextView mErrorTxt;

    private List<DefibrillatorModel> mDefibrillators;
    private ClusterManager<DefibrillatorClusterItem> mClusterManager;
    private PointQuadTree<DefibrillatorClusterItem> mPointQuadTree;
    private HashMap<String, DefibrillatorModel> mMapDefibrillators;

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

    private LocationClient mLocationClient;
    // Define an object that holds accuracy and frequency parameters
    private LocationRequest mLocationRequest;
    private Location mCurrentLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        mErrorTxt = (TextView) findViewById(R.id.error_txt);

        // Setup some defibrillators
        mDefibrillators = DummyDefibrillators.ITEMS;
        mPointQuadTree = new PointQuadTree<DefibrillatorClusterItem>(
                HautRhinUtils.LEFT_BOUND,
                HautRhinUtils.RIGHT_BOUND,
                HautRhinUtils.BOTTOM_BOUND,
                HautRhinUtils.TOP_BOUND);
        mMapDefibrillators = new HashMap<String, DefibrillatorModel>();

        // Setup the map
        setUpMapIfNeeded();

        /*
         * Create a new location client, using the enclosing class to
         * handle callbacks.
         */
        mLocationClient = new LocationClient(this, this, this);

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

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /*
     * Called when the Activity becomes visible.
     */
    @Override
    protected void onStart() {
        super.onStart();
        if(isGooglePlayServicesAvailable(true)) {
            // Connect the client.
            mLocationClient.connect();
        }
    }

    /*
     * Called when the Activity is no longer visible.
     */
    @Override
    protected void onStop() {
        // If the client is connected
        if (mLocationClient.isConnected()) {
            /*
             * Remove location updates for a listener.
             * The current Activity is the listener, so
             * the argument is "this".
             */
            mLocationClient.removeLocationUpdates(this);
        }
        /*
         * After disconnect() is called, the client is
         * considered "dead".
         */
        mLocationClient.disconnect();
        super.onStop();
    }

    /*
     * Handle results returned to the FragmentActivity
     * by Google Play services
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Decide what to do based on the original request code
        switch (requestCode) {
            case GP_CONNECTION_FAILURE_RESOLUTION_REQUEST :
                /*
                 * If the result code is Activity.RESULT_OK, try
                 * to connect again
                 */
                switch (resultCode) {
                    case Activity.RESULT_OK :
                    /*
                     * Try the request again
                     */
                        if(isGooglePlayServicesAvailable(false)) {
                            mLocationClient.connect();
                        }
                        else {
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
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(this);

        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            Log.d(TAG, "Google Play services is available.");
            return true;
        } else {
            if(showErrorDialog) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this, GP_CONNECTION_FAILURE_RESOLUTION_REQUEST).show();
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
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera.
     *
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        mMap.setInfoWindowAdapter(new DefibrillatorInfoWindowAdapter());

        // Enable compass
        UiSettings uiMapSettings = mMap.getUiSettings();
        uiMapSettings.setCompassEnabled(true);
        // Disable default my location button
        uiMapSettings.setMyLocationButtonEnabled(false);

        // Display my location
        mMap.setMyLocationEnabled(true);

        setupClusterer();
    }

    private void setupClusterer() {
        // Initialize the manager with the context and the map.
        // (Activity extends context, so we can pass 'this' in the constructor.)
        mClusterManager = new ClusterManager<DefibrillatorClusterItem>(this, getMap());
        mClusterManager.setRenderer(new DefibrillatorClusterRenderer(this, getMap(), mClusterManager));
        mClusterManager.setOnClusterItemInfoWindowClickListener(this);

        // Point the map's listeners at the listeners implemented by the cluster
        // manager.
        getMap().setOnCameraChangeListener(mClusterManager);
        getMap().setOnMarkerClickListener(mClusterManager);
        getMap().setOnInfoWindowClickListener(mClusterManager);

        addDefibrillatorsMarkers();
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

    private void animateCameraToHautRhin() {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(47.8657299, 7.2315736), 8));
    }

    private void animateCameraToCurrentLocation() {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(MapUtils.getLatLng(mCurrentLocation), 15));
    }


    public void onClickClosestBtn(View view) {
        new GetClosestDefibrillatorAsyncTask().execute();
    }

    public void onClickMyLocationBtn(View view) {
        if(mCurrentLocation != null) {
            animateCameraToCurrentLocation();
        }
    }
    public void onClickHautRhinBtn(View view) {
        animateCameraToHautRhin();
    }

    /*
     * Called by Location Services when the request to connect the
     * client finishes successfully. At this point, you can
     * request the current location or start periodic updates
     */
    @Override
    public void onConnected(Bundle bundle) {
        Log.d(TAG, "Location services connected");

        // Get last location
        updateCurrentLocation(mLocationClient.getLastLocation());
        mLocationClient.requestLocationUpdates(mLocationRequest, this);
    }

    /*
     * Called by Location Services if the connection to the
     * location client drops because of an error.
     */
    @Override
    public void onDisconnected() {
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

    @Override
    public void onLocationChanged(Location location) {
        updateCurrentLocation(location);
    }

    private DefibrillatorClusterItem getClosestDefibrillator(Bounds maxBoundsSearch) {
        if(mCurrentLocation == null) {
            return null;
        }

        double incrementRadian = 0.005;
        double minX = mCurrentLocation.getLongitude() - incrementRadian;
        double maxX = mCurrentLocation.getLongitude() + incrementRadian;
        double minY = mCurrentLocation.getLatitude() - incrementRadian;
        double maxY = mCurrentLocation.getLatitude() + incrementRadian;

        Bounds boundsSearch = new Bounds(minX, maxX, minY, maxY);

        ArrayList<DefibrillatorClusterItem> closeDefibrillators = (ArrayList<DefibrillatorClusterItem>)
                mPointQuadTree.search(boundsSearch);
        Log.d(TAG, "Got " + closeDefibrillators.size() + " close defibrillator");

        Log.d(TAG, "Looking for defib in bounds minX:" + minX + " maxX:" + maxX + " minY:" + minY + " maxY:" + maxY);

        while(closeDefibrillators.size() < 1 &&
                (minX >= maxBoundsSearch.minX ||
                maxX <= maxBoundsSearch.maxX ||
                minY >= maxBoundsSearch.minY ||
                maxY <= maxBoundsSearch.maxY)) {

            if(minX > maxBoundsSearch.minX) {
                minX -= incrementRadian;
            }
            if(maxX < maxBoundsSearch.maxX) {
                maxX += incrementRadian;
            }
            if(minY > maxBoundsSearch.minY) {
                minY -= incrementRadian;
            }
            if(maxY < maxBoundsSearch.maxY) {
                maxY += incrementRadian;
            }

            Log.d(TAG, "Looking for defib in bounds minX:" + minX + " maxX:" + maxX + " minY:" + minY + " maxY:" + maxY);
            boundsSearch = new Bounds(minX, maxX, minY, maxY);
            closeDefibrillators = (ArrayList<DefibrillatorClusterItem>)
                    mPointQuadTree.search(boundsSearch);
        }

        Log.d(TAG, "After the loop Got " + closeDefibrillators.size() + " close defibrillators");

        DefibrillatorClusterItem closestDefibrillator = null;
        // If a defib has been found
        if(closeDefibrillators.size() > 0) {
            // Get the distance
            LatLng currentLocationLatLng = MapUtils.getLatLng(mCurrentLocation);
            closestDefibrillator = closeDefibrillators.get(0);
            double minDistance = SphericalUtil.computeDistanceBetween(currentLocationLatLng, MapUtils.getLatLng(closestDefibrillator.getLatitude(), closestDefibrillator.getLongitude()));

            // If there are more than 1 defib found
            if(closeDefibrillators.size() > 1) {
                DefibrillatorClusterItem closeDefibrillator;

                // Get the closest one
                double distance;
                for (int index = 0; index < closeDefibrillators.size(); index++) {
                    closeDefibrillator = closeDefibrillators.get(index);
                    distance = SphericalUtil.computeDistanceBetween(currentLocationLatLng, MapUtils.getLatLng(closeDefibrillator.getLatitude(), closeDefibrillator.getLongitude()));
                    if(distance < minDistance) {
                        minDistance = distance;
                        closestDefibrillator = closeDefibrillator;
                    }
                }
            }
        }

        return closestDefibrillator;
    }

    private void updateCurrentLocation(Location location) {
        if(location != null) {
            if(mCurrentLocation != null) {
                if(mCurrentLocation.getLongitude() != location.getLongitude() ||
                        mCurrentLocation.getLatitude() != location.getLatitude()) {
                    Log.d(TAG, "Current location updated: " + location.toString());
                    mCurrentLocation = location;
                    onReallyNewCurrentLocation();
                }
            }
            else {
                Log.d(TAG, "Current location updated: " + location.toString());
                mCurrentLocation = location;
                onReallyNewCurrentLocation();
            }
            hideErrorMessage();
        }
        else {
            Log.e(TAG, "Current location unknown");

            showErrorMessage("Votre localisation est inconnue");
        }
    }

    private void onReallyNewCurrentLocation() {
        if(isLocationInHautRhin(mCurrentLocation)) {
            Toast.makeText(this, "Dans le haut rhin", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "Hors du haut rhin", Toast.LENGTH_LONG).show();
        }
        drawCircleWalkingPerimeter();
    }

    private boolean isLocationInHautRhin(Location location) {
        return MapUtils.isLocationInBounds(location, HautRhinUtils.getLatLngBounds());
    }

    @Override
    public void onClusterItemInfoWindowClick(DefibrillatorClusterItem item) {
        Log.d(TAG, "onClusterItemInfoWindowClick");

        Intent intentWalkingDirections = MapUtils.getIntentGoogleMap(item.getPosition());
        // Create intent to show chooser
        Intent chooserMapApplication = Intent.createChooser(intentWalkingDirections, "Display walking directions via:");

        // Verify the intent will resolve to at least one activity
        if (intentWalkingDirections.resolveActivity(getPackageManager()) != null) {
            startActivity(chooserMapApplication);
        }
    }

    private void drawCircleWalkingPerimeter() {
        LatLng currentLocationLatLng = new LatLng(mCurrentLocation.getLatitude(), mCurrentLocation.getLongitude());
        if(mCircleWalkingPerimeter == null) {
            // Instantiates a new CircleOptions object and defines the center and radius
            CircleOptions circleOptions = new CircleOptions()
                    .center(currentLocationLatLng)
                    .strokeColor(Color.argb(100, 0, 255, 0))
                    .radius(400); // In meters
            // Get back the mutable Circle
            mCircleWalkingPerimeter = mMap.addCircle(circleOptions);
        }
        else {
            mCircleWalkingPerimeter.setCenter(currentLocationLatLng);
        }
    }

    private class GetClosestDefibrillatorAsyncTask extends AsyncTask<Void, Void, DefibrillatorClusterItem> {

        @Override
        protected DefibrillatorClusterItem doInBackground(Void... params) {
            return getClosestDefibrillator(HautRhinUtils.getBounds());
        }

        @Override
        protected void onPostExecute(DefibrillatorClusterItem closestDefib) {
            super.onPostExecute(closestDefib);

            if(closestDefib != null) {
                Log.d(TAG, "Got it: " + closestDefib.getLocationDescription());

                LatLng currentLocationLatLng = MapUtils.getLatLng(mCurrentLocation);

                // Draw a line between location and defibrillator
                PolylineOptions rectOptions = new PolylineOptions()
                        .add(closestDefib.getPosition())
                        .add(currentLocationLatLng)
                        .width(3)
                        .color(Color.BLUE);
                mWalkingPerimeterCircle = mMap.addPolyline(rectOptions);

                LatLngBounds boundsCurrentLocationAndDefibrillator =
                        MapUtils.getLatLngBounds(currentLocationLatLng, closestDefib.getPosition());
                mMap.animateCamera(
                        CameraUpdateFactory.newLatLngBounds(boundsCurrentLocationAndDefibrillator,
                        UiUtils.dpToPx(MapActivity.this, 100)));
            }
            else {
                Log.d(TAG, "Couldn't find closest defib");
            }
        }
    }

    class DefibrillatorInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {
        private View mInfoContentsView;

        DefibrillatorInfoWindowAdapter() {
            mInfoContentsView = MapActivity.this.getLayoutInflater().inflate(R.layout.info_window_content_defibrillator, null);
        }

        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            String markerId = marker.getTitle();
            Log.d(TAG, "Marker id: " + markerId);
            final DefibrillatorModel defibrillatorModel = MapActivity.this.mMapDefibrillators.get(markerId);
            if(defibrillatorModel == null) {
                Log.e(TAG, "Defibrillator " + markerId + " unknown");
                return null;
            }

            String environment;
            if (defibrillatorModel.getEnvironment() == EnvironmentEnum.OUTDOORS) {
                environment = "Extérieur";
            }
            else {
                environment = "Intérieur";
            }

            TextView txtDescription = ((TextView) mInfoContentsView.findViewById(R.id.txt_description));
            TextView txtEnvironment = ((TextView) mInfoContentsView.findViewById(R.id.txt_environment));
            TextView txtAddress = ((TextView) mInfoContentsView.findViewById(R.id.txt_address));

            txtDescription.setText(defibrillatorModel.getLocationDescription());
            txtEnvironment.setText(environment);
            txtAddress.setText("Lat/Lng: " + defibrillatorModel.getLatitude() + "," + defibrillatorModel.getLongitude());

            return mInfoContentsView;
        }
    }

    private void showErrorMessage(String message) {
        if(mErrorTxt.getText() != message) {
            mErrorTxt.setText(message);
        }
        if(mErrorTxt.getVisibility() == View.GONE) {
            mErrorTxt.setVisibility(View.VISIBLE);
        }
    }

    private void hideErrorMessage() {
        if(mErrorTxt.getVisibility() == View.VISIBLE) {
            mErrorTxt.setVisibility(View.GONE);
        }
    }
}
