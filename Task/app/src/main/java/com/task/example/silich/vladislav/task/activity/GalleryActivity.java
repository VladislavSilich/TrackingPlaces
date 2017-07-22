package com.task.example.silich.vladislav.task.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
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

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
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
    String[] masRef = new String[10];
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

            for (int i = 0; i < photoReference.size(); i++){
                masRef[i] ="https://maps.googleapis.com/maps/api/place/photo?maxwidth=1024&maxheight=645&photoreference=" +
                        photoReference.get(i) + "&key=AIzaSyCDduz-_Pe51uFLJi0GeKQT7vrpjoZHCYI";
            }
            AsyncTaskGmail photo = new AsyncTaskGmail();
            photo.execute(masRef);

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
class AsyncTaskGmail extends AsyncTask<String,Void,Bitmap[]>{


    @Override
    protected Bitmap[] doInBackground(String... params) {
        Bitmap [] bitmap = new Bitmap[10];
        try {
            for (int i = 0; i < params.length; i++) {
                URL url = null;
                // String imageurl = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=1024&maxheight=645&photoreference=CmRaAAAANo744JaZUr7rVglK74HhpRu_whlUQ12lNAbxv-RNEx0Yboy831ElNVATx5eHbu3PaioDlSIm-cXDfI_vk3PZjC95K8xdb8YlyV7KPZamMKakTNHyg6qwL0PgGUL4Yqo5EhCe-kK6DeJkMOiiho_0rsBzGhTfjHbmtvWu03ZV-uaKy4HMQaD_xg&key=AIzaSyCDduz-_Pe51uFLJi0GeKQT7vrpjoZHCYI";
                Bitmap bm = null;
                InputStream is = null;
                BufferedInputStream bis = null;
                String imageurl = null;
                imageurl = params[i];
                try {
                    URLConnection conn = new URL(imageurl).openConnection();
                    conn.connect();
                    is = conn.getInputStream();
                    bis = new BufferedInputStream(is, 8192);
                    bm = BitmapFactory.decodeStream(bis);
                    bitmap[i] = bm;
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (bis != null) {
                        try {
                            bis.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (is != null) {
                        try {
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }

            }

        }
        catch(Exception e){
                e.printStackTrace();
            }

        return bitmap;
    }

    @Override
    protected void onPostExecute(Bitmap[] bm) {
        super.onPostExecute(bm);
        showPhotoP(bm);

    }
}

    private void showPhotoP(Bitmap [] bm) {
        //Uri[] urimas = new Uri[10];
        ArrayList<Uri> urimas = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Bitmap mutableBitmap = bm[i].copy(Bitmap.Config.ARGB_8888, true);
            View view = new View(this);
            view.draw(new Canvas(mutableBitmap));
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), mutableBitmap, "rbt", null);
            Uri uri = Uri.parse(path);
            urimas.add(uri);
        }
            urimas.size();
            Intent intent = new Intent();
            intent.setType("image/*");
            intent.setAction(Intent.ACTION_SEND_MULTIPLE);
            intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, urimas);
            intent.putExtra(Intent.EXTRA_EMAIL, new String[]{"vladislav.silich.1996@gmail.com"});
            intent.putExtra(Intent.EXTRA_SUBJECT, "sad");
            intent.putExtra(Intent.EXTRA_TEXT, "sdasd");
            intent.setPackage("com.google.android.gm");
            startActivity(intent);
        }
    }


