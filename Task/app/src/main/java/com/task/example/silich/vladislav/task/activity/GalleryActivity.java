package com.task.example.silich.vladislav.task.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.task.example.silich.vladislav.task.R;
import com.task.example.silich.vladislav.task.adapter.RecyclerAdapter;
import com.task.example.silich.vladislav.task.manager.DataManager;
import com.task.example.silich.vladislav.task.network.responce.ResponceSearchPlaces;

import java.net.URL;
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
    ImageView img;
    ImageButton imgGmail;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private static final String API_KEY = "AIzaSyCDduz-_Pe51uFLJi0GeKQT7vrpjoZHCYI";
    private static final int RADIUS = 50000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gallery);
        img = (ImageView)findViewById(imgPhoto);
        imgGmail = (ImageButton)findViewById(R.id.imageGoogle);
        imgGmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(GalleryActivity.this,"ddfsdfsdfs",Toast.LENGTH_LONG).show();
                sendPhoto();
            }
        });
        getSupportActionBar().hide();
         location = getIntent().getStringExtra("location");
        photoReference = new ArrayList<>();
          address = new ArrayList<>();
         dataManager = DataManager.getInstnce();
        getReference();
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        // используем linear layout manager
        mLayoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerAdapter(photoReference,this);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void sendPhoto() {
        URL url = null;
        String imageurl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=1024&maxheight=645&photoreference=CmRaAAAANo744JaZUr7rVglK74HhpRu_whlUQ12lNAbxv-RNEx0Yboy831ElNVATx5eHbu3PaioDlSIm-cXDfI_vk3PZjC95K8xdb8YlyV7KPZamMKakTNHyg6qwL0PgGUL4Yqo5EhCe-kK6DeJkMOiiho_0rsBzGhTfjHbmtvWu03ZV-uaKy4HMQaD_xg&key=AIzaSyCDduz-_Pe51uFLJi0GeKQT7vrpjoZHCYI";
        img.getDrawable();
        //emailIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("https://maps.googleapis.com/maps/api/place/photo?maxwidth=1024&maxheight=645&photoreference=CmRaAAAANo744JaZUr7rVglK74HhpRu_whlUQ12lNAbxv-RNEx0Yboy831ElNVATx5eHbu3PaioDlSIm-cXDfI_vk3PZjC95K8xdb8YlyV7KPZamMKakTNHyg6qwL0PgGUL4Yqo5EhCe-kK6DeJkMOiiho_0rsBzGhTfjHbmtvWu03ZV-uaKy4HMQaD_xg&key=AIzaSyCDduz-_Pe51uFLJi0GeKQT7vrpjoZHCYI"));
    }

    private void getReference() {
        Call<ResponceSearchPlaces> call = dataManager.getPlaceReference(location,RADIUS,API_KEY);
        call.enqueue(new Callback<ResponceSearchPlaces>() {
            @Override
            public void onResponse(Call<ResponceSearchPlaces> call, Response<ResponceSearchPlaces> response) {
                response.body();
                for (int i = 0; i < response.body().getResults().size(); i++){
                    if (response.body().getResults().get(i).getPhotos() != null && photoReference.size() < 4) {
                        photoReference.add(response.body().getResults().get(i).getPhotos().get(0).getPhotoReference());
                        address.add(response.body().getResults().get(i).getVicinity());
                    }
                    else {
                       i++;
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
        mAdapter = new RecyclerAdapter(photoReference,this);
        mRecyclerView.setAdapter(mAdapter);

    }



}
