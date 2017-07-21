package com.task.example.silich.vladislav.task.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.task.example.silich.vladislav.task.R;

import java.util.ArrayList;

/**
 * Created by Lenovo on 19.07.2017.
 */

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {

    private ArrayList<String> mDataset;
    Context mContext;


    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imgPhoto;

        public ViewHolder(View v) {
            super(v);
          imgPhoto = (ImageView)v.findViewById(R.id.imgPhoto);
        }
    }

    // Конструктор
    public RecyclerAdapter(ArrayList<String> dataset, Context context) {
        mDataset = dataset;
        mContext = context;
    }

    // Создает новые views (вызывается layout manager-ом)
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_layout, parent, false);

        // тут можно программно менять атрибуты лэйаута (size, margins, paddings и др.)

        ViewHolder vh = new ViewHolder(v);
        return vh;
    }
   // https://maps.googleapis.com/maps/api/place/photo?maxwidth=1024&maxheight=645&photoreference=&key=AIzaSyCDduz-_Pe51uFLJi0GeKQT7vrpjoZHCYI
    // Заменяет контент отдельного view (вызывается layout manager-ом)
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        String url ="https://maps.googleapis.com/maps/api/place/photo?maxwidth=1024&maxheight=645&photoreference=" +
                mDataset.get(position) + "&key=AIzaSyCDduz-_Pe51uFLJi0GeKQT7vrpjoZHCYI";

        Glide.with(mContext).load(url).into(holder.imgPhoto);

    }

    // Возвращает размер данных (вызывается layout manager-ом)
    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}