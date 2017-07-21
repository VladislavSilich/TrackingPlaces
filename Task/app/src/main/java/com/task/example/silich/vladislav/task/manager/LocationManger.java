package com.task.example.silich.vladislav.task.manager;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

/**
 * Created by Lenovo on 18.07.2017.
 */

public class LocationManger implements
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {
    private final String TAG = "MyAwesomeApp";
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private static Context context;
    private LocationManger() {
    }
    private static LocationManger manager;
    public static LocationManger getInstance(Context _context) {
        if (manager == null) {
            manager = new LocationManger();
        }
        context = _context;
        return manager;
    }
    public void setUpLocation() {
        mGoogleApiClient = new GoogleApiClient.Builder(context)
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }
    @Override
    public void onConnected(Bundle bundle) {
        if (servicesConnected()) {
            startPeriodicUpdates();
        }
    }
    private void startPeriodicUpdates() {
        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(10000); // Update location every second
        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, this);
    }
    /**
     * In response to a request to stop updates, send a request to
     * Location Services
     */
    private void stopPeriodicUpdates() {
        if (mGoogleApiClient.isConnected())
            System.out.println("destroy2");
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }
    public void stopLocationUpdate() {
        stopPeriodicUpdates();
    }
    private boolean servicesConnected() {
        // Check that Google Play services is available
        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(context);
        // If Google Play services is available
        if (ConnectionResult.SUCCESS == resultCode) {
            // In debug mode, log the status
            // Continue
            return true;
            // Google Play services was not available for some reason
        } else {
            // Display an error dialog
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(resultCode, (Activity) context, 0);
            if (dialog != null) {
            }
            return false;
        }
    }
    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection has been suspend");
    }
    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "GoogleApiClient connection has failed");
        ((LocationFound) context).locationFailed();
    }
    @Override
    public void onLocationChanged(Location location) {
        ((LocationFound) context).locationFound(location);
        stopLocationUpdate();                //stop location update. comment this line if you wish to get updated location latitude and longitude
    }
    public interface LocationFound {
        public void locationFound(Location location);
        public void locationFailed();
    }
}
