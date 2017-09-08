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
    String[] masReference = new String[10];
    ImageView img;
    ImageButton imgGmail,imgWhatsApp;
    int CountSocial = 0;
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
                Toast.makeText(GalleryActivity.this,"Please, wait.",Toast.LENGTH_LONG).show();
                CountSocial = 1;
                createPicturesArray();
            }
        });
        imgWhatsApp = (ImageButton)findViewById(R.id.imageWhatsApp);
        imgWhatsApp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CountSocial = 2;
                createPicturesArray();
                Toast.makeText(GalleryActivity.this,"Please, wait",Toast.LENGTH_LONG).show();
            }
        });
        getSupportActionBar().hide();
         location = getIntent().getStringExtra("location");
        photoReference = new ArrayList<>();
         dataManager = DataManager.getInstnce();
        getReference();
        mRecyclerView = (RecyclerView)findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new RecyclerAdapter(photoReference,this);
        mRecyclerView.setAdapter(mAdapter);


    }



    private void createPicturesArray() {
        for (int i = 0; i < photoReference.size(); i++){
                masReference[i] ="https://maps.googleapis.com/maps/api/place/photo?maxwidth=1024&maxheight=645&photoreference=" +
                        photoReference.get(i) + "&key=AIzaSyCDduz-_Pe51uFLJi0GeKQT7vrpjoZHCYI";
            }
                AsyncTaskGmail photo = new AsyncTaskGmail();
                photo.execute(masReference);
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
        mAdapter = new RecyclerAdapter(photoReference,this);
        mRecyclerView.setAdapter(mAdapter);

    }
class AsyncTaskGmail extends AsyncTask<String,Void,Bitmap[]>{

    @Override
    protected Bitmap[] doInBackground(String... params) {
        Bitmap [] bitmapPictures = new Bitmap[10];
        try {
            for (int i = 0; i < params.length; i++) {
                Bitmap bm = null;
                InputStream is = null;
                BufferedInputStream bis = null;
                String imageUrl = null;
                imageUrl = params[i];
                try {
                    URLConnection conn = new URL(imageUrl).openConnection();
                    conn.connect();
                    is = conn.getInputStream();
                    bis = new BufferedInputStream(is, 8192);
                    bm = BitmapFactory.decodeStream(bis);
                    bitmapPictures[i] = bm;
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

        return bitmapPictures;
    }

    @Override
    protected void onPostExecute(Bitmap[] bm) {
        super.onPostExecute(bm);
        if (CountSocial == 1 ){
            sendGmailPictures(bm);
        }else if (CountSocial == 2){
            sendWhatsApp(bm);
        }
    }
}

    private void sendWhatsApp(Bitmap[] bm) {
        ArrayList<Uri> uriList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Bitmap mutableBitmap = bm[i].copy(Bitmap.Config.ARGB_8888, true);
            View view = new View(this);
            view.draw(new Canvas(mutableBitmap));
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), mutableBitmap, "rbt", null);
            Uri uri = Uri.parse(path);
            uriList.add(uri);
        }
        uriList.size();
        try {
            Intent intentWhatsApp = new Intent();
            intentWhatsApp.setType("image/*");
            intentWhatsApp.setAction(Intent.ACTION_SEND_MULTIPLE);
            intentWhatsApp.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
            intentWhatsApp.setPackage("com.whatsapp");
            startActivity(intentWhatsApp);
        }
        catch (Exception e){
            Toast.makeText(GalleryActivity.this,"Sorry...You don't have any WhatsApp app",Toast.LENGTH_LONG).show();
        }
    }

    private void sendGmailPictures(Bitmap [] bm) {
        ArrayList<Uri> uriList = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            Bitmap mutableBitmap = bm[i].copy(Bitmap.Config.ARGB_8888, true);
            View view = new View(this);
            view.draw(new Canvas(mutableBitmap));
            String path = MediaStore.Images.Media.insertImage(getContentResolver(), mutableBitmap, "rbt", null);
            Uri uri = Uri.parse(path);
            uriList.add(uri);
        }
            uriList.size();
        try {
            Intent intentGmail = new Intent();
            intentGmail.setType("image/*");
            intentGmail.setAction(Intent.ACTION_SEND_MULTIPLE);
            intentGmail.putParcelableArrayListExtra(Intent.EXTRA_STREAM, uriList);
            intentGmail.setPackage("com.google.android.gm");
            startActivity(intentGmail);
        }
        catch (Exception e){
            Toast.makeText(GalleryActivity.this,"Sorry...You don't have any mail app",Toast.LENGTH_LONG).show();
        }
        }
    }




