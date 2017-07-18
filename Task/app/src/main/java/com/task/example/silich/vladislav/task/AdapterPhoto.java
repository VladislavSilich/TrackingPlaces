package com.task.example.silich.vladislav.task;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;

/**
 * Created by Lenovo on 18.07.2017.
 */

public class AdapterPhoto extends RecyclerView.Adapter<AdapterPhoto.ViewHolder>{



    ArrayList<String > photo;
    ArrayList<String> address;
    Context context;

    AdapterPhoto(ArrayList<String> photo, ArrayList<String>address, Context context){
        this.photo = photo;
        this.address = address;
        this.context = context;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_layout,parent,false);
        return new ViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final String post = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" +
                photo.get(position) + "&key=AIzaSyCDduz-_Pe51uFLJi0GeKQT7vrpjoZHCYI";

        Glide.with(context).load(post).thumbnail(0.5f)
                .crossFade()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.imgPhoto);
    }

    @Override
    public int getItemCount() {
        return 0;
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        public ViewHolder(View itemView) {
            super(itemView);
            imgPhoto = (ImageView)itemView.findViewById(R.id.imgPhoto);
        }
    }
}
