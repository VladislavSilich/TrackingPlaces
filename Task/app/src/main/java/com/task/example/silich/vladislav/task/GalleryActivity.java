package com.task.example.silich.vladislav.task;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.ImageView;

import com.task.example.silich.vladislav.task.network.responce.ResponceSearchPlaces;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.task.example.silich.vladislav.task.R.id.imgPhoto;

public class GalleryActivity extends AppCompatActivity {
    String location;
     DataManager dataManager;
     ArrayList<String> photoReference;
     ArrayList<String > address;
    RecyclerView recyclerView;
    RecyclerView.LayoutManager recyclerViewLayoutManager;
    RecyclerView.Adapter recyclerView_Adapter;
    ImageView img;
    private static final String API_KEY = "AIzaSyCDduz-_Pe51uFLJi0GeKQT7vrpjoZHCYI";
    private static final int RADIUS = 50000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        img = (ImageView)findViewById(imgPhoto);
         location = getIntent().getStringExtra("location");
        photoReference = new ArrayList<>();
          address = new ArrayList<>();
         dataManager = DataManager.getInstnce();
       recyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        recyclerViewLayoutManager = new GridLayoutManager(GalleryActivity.this,2);
        recyclerView.setLayoutManager(recyclerViewLayoutManager);
        getReference();
    }

    private void getReference() {
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
                showPhoto();
            }

            @Override
            public void onFailure(Call<ResponceSearchPlaces> call, Throwable t) {

            }
        });
    }
    private void showPhoto() {
          photoReference.size();
           address.size();
        recyclerView_Adapter = new AdapterPhoto(photoReference,address,GalleryActivity.this);
        recyclerView.setAdapter(recyclerView_Adapter);
    }
}
