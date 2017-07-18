package com.task.example.silich.vladislav.task;

import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.task.example.silich.vladislav.task.network.responce.ResponceSearchPlaces;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapActivity extends AppCompatActivity implements LocationManger.LocationFound {
    SupportMapFragment mapFragment;
    GoogleMap googleMap;
    double latitude;
    double longitude;
    DataManager dataManager;
    ArrayList<String> photoReference;
    ArrayList<String > address;
    private static final String API_KEY = "AIzaSyCDduz-_Pe51uFLJi0GeKQT7vrpjoZHCYI";
    private static final int RADIUS = 50000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        LocationManger locationManger=LocationManger.getInstance(this);
        locationManger.setUpLocation();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        photoReference = new ArrayList<>();
        address = new ArrayList<>();
        dataManager = DataManager.getInstnce();
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.mapView);

        googleMap = mapFragment.getMap();
        if (googleMap == null){
            finish();
            return;
        }


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String location = latitude+","+longitude;
                Call<ResponceSearchPlaces> call = dataManager.getPlaceReference(location,RADIUS,API_KEY);
                call.enqueue(new Callback<ResponceSearchPlaces>() {
                    @Override
                    public void onResponse(Call<ResponceSearchPlaces> call, Response<ResponceSearchPlaces> response) {
                        response.body();
                        int a = response.body().getResults().size();
                        for (int i = 0; i < response.body().getResults().size(); i++){
                            if (response.body().getResults().get(i).getPhotos() == null) {
                                i++;
                            }
                            else {
                                photoReference.add(response.body().getResults().get(i).getPhotos().get(0).getPhotoReference());
                                address.add(response.body().getResults().get(i).getVicinity());
                            }
                        }
                            getPhoto();
                    }

                    @Override
                    public void onFailure(Call<ResponceSearchPlaces> call, Throwable t) {

                    }
                });
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    private void getPhoto() {
        photoReference.size();
        address.size();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_map, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    @Override
    public void locationFound(Location location) {
        Float speed=location.getSpeed();
         latitude=location.getLatitude();
         longitude=location.getLongitude();
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(latitude, longitude))
                .zoom(13)
                .bearing(45)
                .tilt(20)
                .build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        googleMap.animateCamera(cameraUpdate);
        googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude,longitude)));
    }

    @Override
    public void locationFailed() {

    }
}
